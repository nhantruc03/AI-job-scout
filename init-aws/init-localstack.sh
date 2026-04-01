#!/bin/bash
awslocal s3 mb s3://target-jd-bucket
awslocal dynamodb create-table \
    --table-name global-job-metadata \
    --attribute-definitions AttributeName=jobId,AttributeType=S \
    --key-schema AttributeName=jobId,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
