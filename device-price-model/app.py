from flask import Flask, request, jsonify
import pickle
import pandas as pd
import os

app = Flask(__name__)


script_dir = os.path.dirname(os.path.realpath(__file__))
pipeline_path = os.path.join(script_dir, 'artifacts', 'best_pipeline.pkl')
with open(pipeline_path, 'rb') as pipeline_file:
    pipeline = pickle.load(pipeline_file)


@app.route('/predict', methods=['POST'])
def predict():
    # Retrieve the JSON data from the request
    device_data = request.get_json()

    # Debug: Check the input data structure
    print("Received device data:", device_data)

    # Convert the dictionary into a DataFrame
    device_df = pd.DataFrame([device_data]) 

    try:
        # Predict the price using the model
        predicted_price = pipeline.predict(device_df)
        
        # Return the result as JSON
        return jsonify(predicted_price=int(predicted_price[0]))
    except Exception as e:
        print("Error during prediction:", str(e))
        return jsonify({"error": "Prediction failed", "message": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5000)
