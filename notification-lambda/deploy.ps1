# Zip the lambda
Compress-Archive -Path "index.js" -DestinationPath "..\\notification-lambda.zip" -Force

# Create the function in LocalStack
aws --endpoint-url=http://localhost:4566 lambda create-function `
    --function-name job-notification-lambda `
    --runtime nodejs18.x `
    --handler index.handler `
    --role arn:aws:iam::000000000000:role/lambda-role `
    --code S3Bucket="__local__",S3Key="C:\Users\Admin\Desktop\java workspace\AI-learning\notification-lambda" `
    --region us-east-1

# Verify creation
aws --endpoint-url=http://localhost:4566 lambda get-function --function-name job-notification-lambda
