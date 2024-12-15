---
title: Sensors API v1.0.0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: widdershins v4.0.1
---

# Sensors API v1.0.0

<!-- Generator: Widdershins v4.0.1 -->

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

API for updating fridge temperature and movement sensors.

Base URLs:

- <a href="https://fridgebridge.simplecharity.com">https://fridgebridge.simplecharity.com</a>

# Authentication

- HTTP Authentication, scheme: bearer

## `POST /fridge/sensor/temperature`

_Update fridge temperature sensor_

Updates the temperature readings of a fridge sensor. Only accessible by fridge administrator.

<h3 id="post__fridge_sensor_temperature-parameters">Parameters</h3>

| Name | In   | Type                                  | Required | Description                |
| ---- | ---- | ------------------------------------- | -------- | -------------------------- |
| body | body | [FridgeTempDTO](#schemafridgetempdto) | true     | Temperature update payload |

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X POST https://fridgebridge.simplecharity.com/fridge/sensor/temperature \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

</TabItem>

<TabItem value="http" label="http">

```http
POST https://fridgebridge.simplecharity.com/fridge/sensor/temperature HTTP/1.1
Host: fridgebridge.simplecharity.com
Content-Type: application/json
Accept: application/json

```

</TabItem>

<TabItem value="javascript" label="javascript">

```javascript
const inputBody = '{
  "fridge_id": 123,
  "sensor_id": 456,
  "temp": 4.5
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('https://fridgebridge.simplecharity.com/fridge/sensor/temperature',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});
```

</TabItem>

<TabItem value="ruby" label="ruby">

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => 'application/json',
  'Authorization' => 'Bearer {access-token}'
}

result = RestClient.post 'https://fridgebridge.simplecharity.com/fridge/sensor/temperature',
  params: {
  }, headers: headers

p JSON.parse(result)

```

</TabItem>

<TabItem value="python" label="python">

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Authorization': 'Bearer {access-token}'
}

r = requests.post('https://fridgebridge.simplecharity.com/fridge/sensor/temperature', headers = headers)

print(r.json())

```

</TabItem>

<TabItem value="php" label="php">

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => 'application/json',
    'Authorization' => 'Bearer {access-token}',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','https://fridgebridge.simplecharity.com/fridge/sensor/temperature', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

</TabItem>

<TabItem value="java" label="java">

```java
URL obj = new URL("https://fridgebridge.simplecharity.com/fridge/sensor/temperature");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

</TabItem>

<TabItem value="go" label="go">

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"application/json"},
        "Authorization": []string{"Bearer {access-token}"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "https://fridgebridge.simplecharity.com/fridge/sensor/temperature", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

</TabItem>

</Tabs>

## Responses

| Status | Meaning                                                                    | Description                           | Schema                            |
| ------ | -------------------------------------------------------------------------- | ------------------------------------- | --------------------------------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)                    | Temperature update sent successfully. | [ApiResponse](#schemaapiresponse) |
| 400    | [Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)           | Invalid request body.                 | [ApiResponse](#schemaapiresponse) |
| 401    | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)            | Unauthorized or not a legal entity.   | [ApiResponse](#schemaapiresponse) |
| 500    | [Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1) | Internal server error.                | [ApiResponse](#schemaapiresponse) |

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "message": "Success"
}
```

</TabItem>

</Tabs>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
BearerAuth
</aside>

## `POST /fridge/sensor/movement`

_Update fridge movement sensor_

Updates the movement readings of a fridge sensor. Only accessible by fridge administrator.

<h3 id="post__fridge_sensor_movement-parameters">Parameters</h3>

| Name | In   | Type                                          | Required | Description             |
| ---- | ---- | --------------------------------------------- | -------- | ----------------------- |
| body | body | [FridgeMovementDTO](#schemafridgemovementdto) | true     | Movement update payload |

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X POST https://fridgebridge.simplecharity.com/fridge/sensor/movement \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer {access-token}'

```

</TabItem>

<TabItem value="http" label="http">

```http
POST https://fridgebridge.simplecharity.com/fridge/sensor/movement HTTP/1.1
Host: fridgebridge.simplecharity.com
Content-Type: application/json
Accept: application/json

```

</TabItem>

<TabItem value="javascript" label="javascript">

```javascript
const inputBody = '{
  "fridge_id": 123,
  "sensor_id": 789,
  "is_moving": true
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json',
  'Authorization':'Bearer {access-token}'
};

fetch('https://fridgebridge.simplecharity.com/fridge/sensor/movement',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});
```

</TabItem>

<TabItem value="ruby" label="ruby">

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => 'application/json',
  'Authorization' => 'Bearer {access-token}'
}

result = RestClient.post 'https://fridgebridge.simplecharity.com/fridge/sensor/movement',
  params: {
  }, headers: headers

p JSON.parse(result)

```

</TabItem>

<TabItem value="python" label="python">

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
  'Authorization': 'Bearer {access-token}'
}

r = requests.post('https://fridgebridge.simplecharity.com/fridge/sensor/movement', headers = headers)

print(r.json())

```

</TabItem>

<TabItem value="php" label="php">

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => 'application/json',
    'Authorization' => 'Bearer {access-token}',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','https://fridgebridge.simplecharity.com/fridge/sensor/movement', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

</TabItem>

<TabItem value="java" label="java">

```java
URL obj = new URL("https://fridgebridge.simplecharity.com/fridge/sensor/movement");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

</TabItem>

<TabItem value="go" label="go">

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"application/json"},
        "Authorization": []string{"Bearer {access-token}"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "https://fridgebridge.simplecharity.com/fridge/sensor/movement", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

</TabItem>

</Tabs>

## Responses

| Status | Meaning                                                                    | Description                         | Schema                            |
| ------ | -------------------------------------------------------------------------- | ----------------------------------- | --------------------------------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)                    | Movement update sent successfully.  | [ApiResponse](#schemaapiresponse) |
| 400    | [Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)           | Invalid request body.               | [ApiResponse](#schemaapiresponse) |
| 401    | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)            | Unauthorized or not a legal entity. | [ApiResponse](#schemaapiresponse) |
| 500    | [Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1) | Internal server error.              | [ApiResponse](#schemaapiresponse) |

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "message": "Success"
}
```

</TabItem>

</Tabs>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
BearerAuth
</aside>

## Schemas

<h3 id="tocS_FridgeTempDTO">FridgeTempDTO</h3>
<!-- backwards compatibility -->
<a id="schemafridgetempdto"></a>
<a id="schema_FridgeTempDTO"></a>
<a id="tocSfridgetempdto"></a>
<a id="tocsfridgetempdto"></a>

```json
{
  "fridge_id": 123,
  "sensor_id": 456,
  "temp": 4.5
}
```

#### Properties

| Name      | Type           | Required | Restrictions | Description |
| --------- | -------------- | -------- | ------------ | ----------- |
| fridge_id | integer        | true     | none         | none        |
| sensor_id | integer        | true     | none         | none        |
| temp      | number(double) | true     | none         | none        |

<h3 id="tocS_FridgeMovementDTO">FridgeMovementDTO</h3>
<!-- backwards compatibility -->
<a id="schemafridgemovementdto"></a>
<a id="schema_FridgeMovementDTO"></a>
<a id="tocSfridgemovementdto"></a>
<a id="tocsfridgemovementdto"></a>

```json
{
  "fridge_id": 123,
  "sensor_id": 789,
  "is_moving": true
}
```

#### Properties

| Name      | Type    | Required | Restrictions | Description |
| --------- | ------- | -------- | ------------ | ----------- |
| fridge_id | integer | true     | none         | none        |
| sensor_id | integer | true     | none         | none        |
| is_moving | boolean | true     | none         | none        |

<h3 id="tocS_ApiResponse">ApiResponse</h3>
<!-- backwards compatibility -->
<a id="schemaapiresponse"></a>
<a id="schema_ApiResponse"></a>
<a id="tocSapiresponse"></a>
<a id="tocsapiresponse"></a>

```json
{
  "status": 200,
  "message": "Success"
}
```

#### Properties

| Name    | Type    | Required | Restrictions | Description |
| ------- | ------- | -------- | ------------ | ----------- |
| status  | integer | false    | none         | none        |
| message | string  | false    | none         | none        |
