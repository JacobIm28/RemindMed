**Building and Deploying to Google Cloud:**

1. ```bash
   gcloud auth login
   ```
2. ```bash
   gcloud config set project remindmed-418122
   ```
3. ```bash
    docker login
    ```
4. ```bash
    gcloud auth configure-docker gcr.io
    ```
5. ```bash
    ./gradlew bootBuildImage
    ```
6. ```bash
    docker push gcr.io/remindmed-418122/remindmed-api
    ```
7. Select the new image in the service on gcloud at:
   https://console.cloud.google.com/run/detail/us-east4/remindmed-api/metrics?project=remindmed-418122