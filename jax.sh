#!/bin/bash

# Function to update Lambda function code
update_lambda_code() {
  aws lambda update-function-code --function-name "$1" --zip-file "fileb:///Users/jaxsonruff/340/tweeter-4B/server/build/libs/server-all.jar" &
}

# Array of common prefixes for Lambda function ARNs
common_prefix="arn:aws:lambda:us-west-2:300626769705:function:"
functions=(
  "fetch_follows"
  "job_handler"
  "login"
  "logout"
  "register"
  "get_following"
  "get_followers"
  "get_feed"
  "get_story"
  "post_status"
  "is_follower"
  "get_followers_count"
  "get_following_count"
  "follow"
  "unfollow"
  "get_user"
)

# Check if parameters are provided
if [ "$#" -eq 0 ]; then

  echo "Update all Lambda functions"
  for function_name in "${functions[@]}"; do
    echo "$function_name"
    update_lambda_code "$common_prefix$function_name"
    sleep 2
  done
else
  echo "Update specific Lambda functions based on parameters"
  for param in "$@"; do
    echo "$param"
    update_lambda_code "$common_prefix$param"
    sleep 1
  done
fi
