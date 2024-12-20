# Tracing Demo Service

This repository simulates tracing and context propagation issues we've observed in our projects 
using Datadog's APM with a Kafka producer/consumer setup and other dependencies.

## Prerequisites
- Docker and Docker Compose installed on your machine.
- A Datadog API key with APM enabled.

---

## Setup Instructions

### Step 1: Clone the Repository
Clone the repository to your local machine:
```bash
git clone https://github.com/richardgalestok-bp/tracingdemo.git
cd tracingdemo
```

---

### Step 2: Update Datadog API Key
Open the `local/docker-compose.yaml` file and replace the placeholder value for `DD_API_KEY` with your Datadog API key:
```yaml
  environment:
    DD_API_KEY: "<YOUR_DATADOG_API_KEY>"
```

---

### Step 3: Start the Application
Navigate to the `local` folder and run the following command to start the necessary containers:
```bash
docker-compose up
```

---

### Step 4: Verify Container Health
Ensure that all the following containers are running and healthy:
- `datadog-agent`
- `db` (DynamoDB)
- `kafka`
- `zookeeper`

---

### Step 5: Run the Application
Execute the `start.sh` script to build and run the application with the required configurations:
```bash
./start.sh
```

This script will:
- Build the application.
- Configure the environment.
- Simulate producing and consuming messages within the application server.

---

### Step 6: View Traces in Datadog
Wait for a few minutes for the Datadog agent to collect traces. Then:
1. Log in to the Datadog UI.
2. Navigate to the **APM** section.
3. Search for the `tracing-demo-service` in the list of services.

---

### Step 7: Analyze Traces
1. Open the latest trace from the **Jobs** operation.
2. Visualize the flame graph to identify trace details.
    - You should see an incorrect context propagated for DynamoDB traces.

---

## Troubleshooting
- Ensure the Datadog agent is properly configured with the correct API key.
- Verify container logs to debug any health issues.
- Check the Datadog APM UI for missing traces and ensure the service is emitting spans.

