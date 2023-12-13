package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.byu.cs.tweeter.server.dao.dynamodb.tables.Table;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public abstract class DynamoDAO<T extends Table> {
    private final Class<T> TABLE_CLASS;
    private final String TABLE_NAME;
    private final String INDEX_NAME;
    protected Key key;
    private DynamoDbTable<T> table;
    private DynamoDbIndex<T> index;
    private T existingEntry;


    public DynamoDAO(Class<T> tableClass, String tableName, String indexName){
        this.TABLE_CLASS = tableClass;
        this.TABLE_NAME = tableName;
        this.INDEX_NAME = indexName;
        constructTableOrIndex(false);
    }
    private final DynamoDbEnhancedClient enhancedClient = TweeterDynamoDBClient.getClient();

   public T find(String partitionValue, String sortValue){
       try{
           key = buildKey(partitionValue, sortValue);
           return getExistingValue();
       } catch (Exception e){
           throw new RuntimeException("[Server Error] Error thrown while fetching "  + " from database (partitionValue=" + partitionValue.toString() + ", sortKey=" + sortValue + "): " + e.getMessage());
       }
   }

   public void createOrOverwrite(T newEntry) {
       try{
           key = buildKey(newEntry.partitionValue(), newEntry.sortValue());
           getExistingValue();
           if (existingEntry == null) {
               System.out.println("Saving new item:" + newEntry);
               table.putItem(newEntry);
           } else {
               System.out.println("Updating item:" + newEntry);
               table.updateItem(newEntry);
           }
       }catch (Exception e){
           throw new RuntimeException("[Server Error] Error thrown while performing createOrUpdate for "  + " in database (partitionValue=" + newEntry.partitionValue() + ", sortKey=" + (newEntry.sortValue() == null ? "null" : newEntry.sortValue().toString()) + "): " + e.getMessage());
       }
   }

    public void update(String partitionValue, String sortValue, Map<String, Integer> updateMap) {
        try{
            key = buildKey(partitionValue, sortValue);
            getExistingValue();
            changeRecordBeforeUpdate(existingEntry, updateMap);
            System.out.println("Updating record to:" + existingEntry);
            table.updateItem(existingEntry);
        }catch (Exception e){
            throw new RuntimeException("[Server Error] Error thrown while performing createOrUpdate for " + " in database (partitionValue=" + partitionValue + ", sortKey=" + sortValue + "): " + e.getMessage());
        }
    }

   public void delete(T entry) {
       try{
           key = buildKey(entry.partitionValue(), entry.sortValue());
           System.out.println("Deleting item (if it exists): " + entry);
           table.deleteItem(key);
       }catch (Exception e){
           throw new RuntimeException("[DAO Error] Error thrown while deleting "  + " from database (partitionValue=" + entry.partitionValue() + ", sortValue=" + entry.sortValue() + "): " + e.getMessage());
       }
   }

    protected boolean getItems(String partitionValue, String sortValue, int pageSize, T lastItem, boolean useIndex, boolean sortBackward, boolean sortForward) {
        constructTableOrIndex(useIndex);
        key = buildKey(partitionValue, sortValue);
        QueryEnhancedRequest.Builder requestBuilder;

        if (sortBackward) {
           requestBuilder = QueryEnhancedRequest.builder()
               .scanIndexForward(false)
               .queryConditional(QueryConditional.keyEqualTo(key))
               .limit(pageSize);
        } else if (sortForward) {
            requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(true)
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);
        } else {
            requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);
        }

        if(lastItem != null) {
            System.out.println("lastItem is not null");
            Object lastItemSortValue = getSortValue(useIndex, lastItem);
            if (isNonEmptyString(sortValue)){
                // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(getPartitionKey(useIndex, lastItem), AttributeValue.builder().s(partitionValue).build());
                startKey.put(getSortKey(useIndex, lastItem), AttributeValue.builder().s((String)lastItemSortValue).build());
                requestBuilder.exclusiveStartKey(startKey);
            }
        } else {
            System.out.println("lastItem is null");
        }
        QueryEnhancedRequest request = requestBuilder.build();
        SdkIterable<Page<T>> sdkIterable = useIndex ? index.query(request) : table.query(request);
        PageIterable<T> pages = PageIterable.create(sdkIterable);

        AtomicBoolean hasMorePages = new AtomicBoolean(false);
        pages.stream()
                .limit(1)
                .forEach((Page<T> page) -> {
                    hasMorePages.set(page.lastEvaluatedKey() != null);
                    page.items().forEach(this::saveItem);
                });
        return hasMorePages.get();
    }

    private Object getSortValue(boolean useIndex, T lastItem) {
       if (useIndex) {
           return lastItem.partitionValue();
       }
       return lastItem.sortValue();
    }

//    ==============================================================================================

    public void createBatchesAndThenWrite(List<T> items) {
       System.out.println("Adding batch");
        List<T> batchToWrite = new ArrayList<>();
        for (T u : items) {
            batchToWrite.add(u);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunk(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunk(batchToWrite);
        }
    }
    private void writeChunk(List<T> items) {
        if(items.size() > 25)
            throw new RuntimeException("Too many items to write");
        System.out.println("Writing chunk of size: " + items.size());

        WriteBatch.Builder<T> writeBuilder = WriteBatch.builder(TABLE_CLASS).mappedTableResource(table);
        for (T item : items) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
            System.out.println("Successful write.." + items.get(0).partitionValue());
            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunk(result.unprocessedPutItemsForTable(table));
                System.out.println("Just hammering out some more" + result.unprocessedPutItemsForTable(table).size());
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


//    ==============================================================================================
    protected abstract void saveItem(T entry);

    private T getExistingValue(){
       existingEntry = table.getItem(key);
       System.out.println("Existing entry: " + existingEntry);
       return existingEntry;
   }

   public abstract void changeRecordBeforeUpdate(T existingEntry, Map<String, Integer> updateObject);

    protected void constructTableOrIndex(boolean useIndex){
        if (useIndex) {
            index = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(TABLE_CLASS)).index(INDEX_NAME);
        } else {
            table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(TABLE_CLASS));
        }
    }

    public static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    private String getPartitionKey(boolean useIndex, T lastItem) {
        return useIndex ? lastItem.sortKey() : lastItem.partitionKey();
    }

    private String getSortKey(boolean useIndex, T lastItem){
        return useIndex ? lastItem.partitionKey() : lastItem.sortKey();
    }

    private Key buildKey(String partitionVal, Object sortVal) {
       if (sortVal == null) {
           return Key.builder()
                   .partitionValue(partitionVal)
                   .build();
       }
       if (sortVal instanceof String) {
           return Key.builder()
                   .partitionValue(partitionVal).sortValue((String)sortVal)
                   .build();
       } else if (sortVal instanceof Number) {
           return Key.builder()
                .partitionValue(partitionVal).sortValue((Number)sortVal)
                .build();
       }
       return null;
    }
}



