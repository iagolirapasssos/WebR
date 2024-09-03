# WebR
The `WebR` extension allows you to perform web requests in MIT App Inventor. You can use it to send GET, POST, PUT, PATCH, and DELETE requests. The extension supports both synchronous and asynchronous operations and provides methods for setting headers, data, and base URLs.

#### Methods

1. **`BaseUrl(String url)`**
   - **Description**: Sets the base URL for web requests.
   - **Parameters**: `url` (String) - The base URL for requests.
   - **Example**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     ```

2. **`Async(boolean async)`**
   - **Description**: Sets whether requests should be asynchronous. Default is true.
   - **Parameters**: `async` (boolean) - `true` for asynchronous, `false` for synchronous.
   - **Example**:
     ```javascript
     webR.Async(true);
     ```

3. **`Headers(YailDictionary headers)`**
   - **Description**: Sets the request headers from a YailDictionary.
   - **Parameters**: `headers` (YailDictionary) - A dictionary containing header names and values.
   - **Example**:
     ```javascript
     YailDictionary headers = new YailDictionary();
     headers.put("Content-Type", "application/json");
     webR.Headers(headers);
     ```

4. **`Data(String data)`**
   - **Description**: Sets the data to be sent with the request.
   - **Parameters**: `data` (String) - The data to be sent with the request.
   - **Example**:
     ```javascript
     webR.Data("{\"key\":\"value\"}");
     ```

5. **`Get(String endpoint, String tag)`**
   - **Description**: Executes a GET request to the specified endpoint.
   - **Parameters**: `endpoint` (String) - The API endpoint relative to the base URL. `tag` (String) - Tag for identifying the request.
   - **Example**:
     ```javascript
     webR.Get("users/1", "getUser");
     ```

6. **`Post(String endpoint, String tag)`**
   - **Description**: Executes a POST request to the specified endpoint.
   - **Parameters**: `endpoint` (String) - The API endpoint relative to the base URL. `tag` (String) - Tag for identifying the request.
   - **Example**:
     ```javascript
     webR.Post("users", "createUser");
     ```

7. **`Put(String endpoint, String tag)`**
   - **Description**: Executes a PUT request to the specified endpoint.
   - **Parameters**: `endpoint` (String) - The API endpoint relative to the base URL. `tag` (String) - Tag for identifying the request.
   - **Example**:
     ```javascript
     webR.Put("users/1", "updateUser");
     ```

8. **`Patch(String endpoint, String tag)`**
   - **Description**: Executes a PATCH request to the specified endpoint.
   - **Parameters**: `endpoint` (String) - The API endpoint relative to the base URL. `tag` (String) - Tag for identifying the request.
   - **Example**:
     ```javascript
     webR.Patch("users/1", "patchUser");
     ```

9. **`Delete(String endpoint, String tag)`**
   - **Description**: Executes a DELETE request to the specified endpoint.
   - **Parameters**: `endpoint` (String) - The API endpoint relative to the base URL. `tag` (String) - Tag for identifying the request.
   - **Example**:
     ```javascript
     webR.Delete("users/1", "deleteUser");
     ```

10. **`JsonToDictionary(String jsonString)`**
    - **Description**: Converts a JSON string to a YailDictionary.
    - **Parameters**: `jsonString` (String) - The JSON string to be converted.
    - **Returns**: A YailDictionary representing the JSON data.
    - **Example**:
      ```javascript
      YailDictionary dict = webR.JsonToDictionary("{\"key\":\"value\"}");
      ```

11. **`DictionaryToJson(YailDictionary dictionary)`**
    - **Description**: Converts a YailDictionary to a JSON string.
    - **Parameters**: `dictionary` (YailDictionary) - The dictionary to be converted.
    - **Returns**: A JSON string representing the dictionary data.
    - **Example**:
      ```javascript
      String json = webR.DictionaryToJson(dict);
      ```

#### cURL Examples and Their Translation to WebR

1. **GET Request**

   - **cURL**:
     ```bash
     curl -X GET "https://api.example.com/users/1" -H "Authorization: Bearer your_token"
     ```

   - **WebR**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     YailDictionary headers = new YailDictionary();
     headers.put("Authorization", "Bearer your_token");
     webR.Headers(headers);
     webR.Get("users/1", "getUser");
     ```

2. **POST Request**

   - **cURL**:
     ```bash
     curl -X POST "https://api.example.com/users" -H "Content-Type: application/json" -d '{"name":"John Doe"}'
     ```

   - **WebR**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     YailDictionary headers = new YailDictionary();
     headers.put("Content-Type", "application/json");
     webR.Headers(headers);
     webR.Data("{\"name\":\"John Doe\"}");
     webR.Post("users", "createUser");
     ```

3. **PUT Request**

   - **cURL**:
     ```bash
     curl -X PUT "https://api.example.com/users/1" -H "Content-Type: application/json" -d '{"name":"Jane Doe"}'
     ```

   - **WebR**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     YailDictionary headers = new YailDictionary();
     headers.put("Content-Type", "application/json");
     webR.Headers(headers);
     webR.Data("{\"name\":\"Jane Doe\"}");
     webR.Put("users/1", "updateUser");
     ```

4. **PATCH Request**

   - **cURL**:
     ```bash
     curl -X PATCH "https://api.example.com/users/1" -H "Content-Type: application/json" -d '{"email":"jane.doe@example.com"}'
     ```

   - **WebR**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     YailDictionary headers = new YailDictionary();
     headers.put("Content-Type", "application/json");
     webR.Headers(headers);
     webR.Data("{\"email\":\"jane.doe@example.com\"}");
     webR.Patch("users/1", "patchUser");
     ```

5. **DELETE Request**

   - **cURL**:
     ```bash
     curl -X DELETE "https://api.example.com/users/1" -H "Authorization: Bearer your_token"
     ```

   - **WebR**:
     ```javascript
     webR.BaseUrl("https://api.example.com/");
     YailDictionary headers = new YailDictionary();
     headers.put("Authorization", "Bearer your_token");
     webR.Headers(headers);
     webR.Delete("users/1", "deleteUser");
     ```

### Summary

The `WebR` extension provides an easy way to handle web requests in MIT App Inventor. By setting the base URL, headers, and request data, you can perform various HTTP methods like GET, POST, PUT, PATCH, and DELETE. The extension also includes methods to convert between JSON strings and YailDictionaries for handling JSON data.

Feel free to reach out if you have any further questions or need additional examples!
