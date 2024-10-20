package com.bosonshiggs.webr;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailDictionary;
import org.json.JSONObject; // Importar a biblioteca JSON
import org.json.JSONArray;   // Importar a biblioteca JSON

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import android.os.Handler;
import android.os.Looper;


@DesignerComponent(version = 2,
        description = "Extension for making web requests with support for GET, POST, PUT, PATCH, DELETE.",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/web.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar") // Include json.jar library
public class WebR extends AndroidNonvisibleComponent {

    private String baseUrl = "";
    private boolean async = true;
    private HashMap<String, String> headers = new HashMap<>();
    private String data = "";
    private final Handler uiHandler;

    public WebR(ComponentContainer container) {
	    super(container.$form());
	    this.uiHandler = new Handler(Looper.getMainLooper());
	}

    /**
     * Sets the base URL for web requests.
     * Throws an error if the URL is null or empty.
     */
    @SimpleProperty(description = "Sets the base URL for web requests.")
    public void BaseUrl(String url) {
        try {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("Base URL cannot be null or empty.");
            }
            this.baseUrl = url;
        } catch (IllegalArgumentException e) {
            Error("BaseUrlError", e.getMessage());
        }
    }
    
    @SimpleProperty(description = "Gets the current base URL.")
    public String BaseUrl() {
        return this.baseUrl;
    }

    /**
     * Sets whether requests should be asynchronous. Default is true.
     */
    @SimpleProperty(description = "Sets whether requests will be asynchronous. Default is true.")
    public void Async(boolean async) {
        this.async = async;
    }
    
    @SimpleProperty(description = "Gets whether the requests are asynchronous.")
    public boolean Async() {
        return this.async;
    }
    

    /**
     * Sets the request headers from a YailDictionary.
     * Throws an error if there is an issue with headers.
     */
    @SimpleProperty(description = "Sets the request headers.")
    public void Headers(YailDictionary headers) {
        try {
            this.headers.clear();
            for (Object key : headers.keySet()) {
                this.headers.put(key.toString(), headers.get(key).toString());
            }
        } catch (Exception e) {
            Error("HeadersError", "Error setting headers: " + e.getMessage());
        }
    }
    
    
    @SimpleProperty(description = "Gets the current request headers as a dictionary.")
    public YailDictionary Headers() {
        YailDictionary headersDict = new YailDictionary();
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            headersDict.put(entry.getKey(), entry.getValue());
        }
        return headersDict;
    }

    /**
     * Sets the data to be sent with the request.
     */
    @SimpleProperty(description = "Sets the request data.")
    public void Data(String data) {
        this.data = data;
    }
    
    @SimpleProperty(description = "Gets the current request data.")
    public String Data() {
        return this.data;
    }

    /**
     * Executes a GET request to the specified endpoint.
     */
    @SimpleFunction(description = "Executes a GET request.")
    public void Get(String endpoint, String tag) {
        executeRequest("GET", endpoint, tag);
    }

    /**
     * Executes a POST request to the specified endpoint.
     */
    @SimpleFunction(description = "Executes a POST request.")
    public void Post(String endpoint, String tag) {
        executeRequest("POST", endpoint, tag);
    }

    /**
     * Executes a PUT request to the specified endpoint.
     */
    @SimpleFunction(description = "Executes a PUT request.")
    public void Put(String endpoint, String tag) {
        executeRequest("PUT", endpoint, tag);
    }

    /**
     * Executes a PATCH request to the specified endpoint.
     */
    @SimpleFunction(description = "Executes a PATCH request.")
    public void Patch(String endpoint, String tag) {
        executeRequest("PATCH", endpoint, tag);
    }

    /**
     * Executes a DELETE request to the specified endpoint.
     */
    @SimpleFunction(description = "Executes a DELETE request.")
    public void Delete(String endpoint, String tag) {
        executeRequest("DELETE", endpoint, tag);
    }

    /**
     * Handles the execution of the web request.
     * If asynchronous, runs the request on a new thread.
     * If synchronous, runs the request on the main thread.
     */
    private void executeRequest(final String method, String endpoint, final String tag) {
        final String urlString = this.baseUrl + endpoint;

        if (urlString == null || urlString.isEmpty()) {
            Error("RequestError", "The URL cannot be null or empty.");
            return;
        }

        // If async is true, run the request on a separate thread
        if (async) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendRequest(method, urlString, tag);
                }
            }).start();
        } else {
            // Execute on the main thread
            sendRequest(method, urlString, tag);
        }
    }

    /**
     * Performs the actual network request and handles the response.
     * Manages input/output streams, processes headers, and handles response codes.
     */
    private void sendRequest(String method, String urlString, String tag) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            // Set request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // For methods other than GET, send request data if available
            if (!method.equals("GET") && !data.isEmpty()) {
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response from the server
            int responseCode = connection.getResponseCode();
            InputStream stream = responseCode >= 200 && responseCode < 400 
                                ? connection.getInputStream() 
                                : connection.getErrorStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Trigger success event if the response code is within the 200 range
            if (responseCode >= 200 && responseCode < 300) {
                RequestCompleted(tag, response.toString());
            } else {
                // Trigger failure event if response code indicates an error
                RequestFailed(tag, "Response code: " + responseCode + ". Response: " + response.toString());
            }

        } catch (MalformedURLException e) {
            // Handle URL formatting errors
            Error("MalformedURLException", "Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            // Handle I/O errors
            RequestFailed(tag, "IO error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected errors
            Error("UnexpectedError", "Unexpected error: " + e.getMessage());
        } finally {
            // Close the reader and disconnect the connection
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Converts a JSON string to a YailDictionary.
     * Throws an error if the JSON is malformed.
     */
    @SimpleFunction(description = "Converts a JSON string to a YailDictionary.")
    public YailDictionary JsonToDictionary(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            YailDictionary dictionary = new YailDictionary();
            for (Object keyObj : jsonObject.keySet()) {
                String key = (String) keyObj; // Cast to String
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    value = JsonToDictionary(value.toString());
                } else if (value instanceof JSONArray) {
                    value = JsonArrayToList((JSONArray) value);
                }
                dictionary.put(key, value.toString());
            }
            return dictionary;
        } catch (Exception e) {
            Error("JsonToDictionaryError", "Error converting JSON to dictionary: " + e.getMessage());
            return new YailDictionary();
        }
    }

    /**
     * Converts a YailDictionary to a JSON string.
     * Throws an error if the conversion fails.
     */
    @SimpleFunction(description = "Converts a YailDictionary to a JSON string.")
    public String DictionaryToJson(YailDictionary dictionary) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (Object key : dictionary.keySet()) {
                String value = dictionary.get(key).toString();
                jsonObject.put(key.toString(), value);
            }
            return jsonObject.toString();
        } catch (Exception e) {
            Error("DictionaryToJsonError", "Error converting dictionary to JSON: " + e.getMessage());
            return "{}";
        }
    }

    /**
     * Converts a JSONArray to a List.
     * Handles nested JSONObjects and JSONArrays.
     */
    private List<Object> JsonArrayToList(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object item = jsonArray.opt(i);
            if (item instanceof JSONObject) {
                item = JsonToDictionary(item.toString());
            } else if (item instanceof JSONArray) {
                item = JsonArrayToList((JSONArray) item);
            }
            list.add(item);
        }
        return list;
    }

    @SimpleEvent(description = "Event triggered when a web request completes successfully.")
	public void RequestCompleted(final String tag, final String response) {
		uiHandler.post(new Runnable() {
		    @Override
		    public void run() {
		        EventDispatcher.dispatchEvent(WebR.this, "RequestCompleted", tag, response);
		    }
		});
	}

    @SimpleEvent(description = "Event triggered when a web request fails.")
	public void RequestFailed(final String tag, final String error) {
		uiHandler.post(new Runnable() {
		    @Override
		    public void run() {
		        EventDispatcher.dispatchEvent(WebR.this, "RequestFailed", tag, error);
		    }
		});
	}

    @SimpleEvent(description = "Event triggered when there is an error in setting a property or performing an action.")
	public void Error(final String errorCode, final String errorMessage) {
		uiHandler.post(new Runnable() {
		    @Override
		    public void run() {
		        EventDispatcher.dispatchEvent(WebR.this, "Error", errorCode, errorMessage);
		    }
		});
	}
}

