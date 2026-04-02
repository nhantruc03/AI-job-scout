exports.handler = async (event) => {
    console.log("Job Update Notification received:", JSON.stringify(event, null, 2));
    
    // Simulate notification
    return {
        statusCode: 200,
        body: JSON.stringify({
            message: "Notification sent successfully!",
            receivedEvent: event
        })
    };
};
