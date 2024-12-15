---
title: Person In Need API v1.0.0
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

<!-- Generator: Widdershins v4.0.1 -->

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

API for handling meal withdrawals and checking card usages.

Base URLs:

* <a href="https://fridgebridge.simplecharity.com">https://fridgebridge.simplecharity.com</a>

## `POST /pin/withdraw`

*Withdraw a meal from the fridge*

Withdraws specified meals if the card and fridge are valid and there are enough usages left.

<h3 id="post__pin_withdraw-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[WithdrawMealDTO](#schemawithdrawmealdto)|true|none|

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X POST https://fridgebridge.simplecharity.com/pin/withdraw \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json'

```
</TabItem>

<TabItem value="http" label="http">

```http
POST https://fridgebridge.simplecharity.com/pin/withdraw HTTP/1.1
Host: fridgebridge.simplecharity.com
Content-Type: application/json
Accept: application/json

```
</TabItem>

<TabItem value="javascript" label="javascript">

```javascript
const inputBody = '{
  "fridge_id": 1,
  "card": {
    "number": "1234567890",
    "security_code": "1234"
  },
  "meals": [
    {
      "id": 42
    }
  ]
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json'
};

fetch('https://fridgebridge.simplecharity.com/pin/withdraw',
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
  'Accept' => 'application/json'
}

result = RestClient.post 'https://fridgebridge.simplecharity.com/pin/withdraw',
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
  'Accept': 'application/json'
}

r = requests.post('https://fridgebridge.simplecharity.com/pin/withdraw', headers = headers)

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
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','https://fridgebridge.simplecharity.com/pin/withdraw', array(
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
URL obj = new URL("https://fridgebridge.simplecharity.com/pin/withdraw");
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
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "https://fridgebridge.simplecharity.com/pin/withdraw", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```
</TabItem>

</Tabs>

## Responses

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Successful meal withdrawal|[ApiResponse](#schemaapiresponse)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Bad request due to invalid card, fridge, or meal|[ApiResponse](#schemaapiresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal server error|[ApiResponse](#schemaapiresponse)|

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "message": "Meals successfully withdrawn",
  "data": null
}
```

</TabItem>

<TabItem value="400 Response" label="400 Response">

```json
{
  "status": 400,
  "message": "The given card does not exist",
  "data": null
}
```

</TabItem>

<TabItem value="500 Response" label="500 Response">

```json
{
  "status": 500,
  "message": "Internal server error",
  "data": null
}
```

</TabItem>

</Tabs>

## `POST /pin/usages`

*Get card usage details*

Retrieves usage details for the given card.

<h3 id="post__pin_usages-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[WithdrawMealDTO](#schemawithdrawmealdto)|true|none|

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X POST https://fridgebridge.simplecharity.com/pin/usages \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json'

```
</TabItem>

<TabItem value="http" label="http">

```http
POST https://fridgebridge.simplecharity.com/pin/usages HTTP/1.1
Host: fridgebridge.simplecharity.com
Content-Type: application/json
Accept: application/json

```
</TabItem>

<TabItem value="javascript" label="javascript">

```javascript
const inputBody = '{
  "fridge_id": 1,
  "card": {
    "number": "1234567890",
    "security_code": "1234"
  },
  "meals": [
    {
      "id": 42
    }
  ]
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json'
};

fetch('https://fridgebridge.simplecharity.com/pin/usages',
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
  'Accept' => 'application/json'
}

result = RestClient.post 'https://fridgebridge.simplecharity.com/pin/usages',
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
  'Accept': 'application/json'
}

r = requests.post('https://fridgebridge.simplecharity.com/pin/usages', headers = headers)

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
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','https://fridgebridge.simplecharity.com/pin/usages', array(
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
URL obj = new URL("https://fridgebridge.simplecharity.com/pin/usages");
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
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "https://fridgebridge.simplecharity.com/pin/usages", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```
</TabItem>

</Tabs>

## Responses

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Successful retrieval of usage details|[ApiResponse](#schemaapiresponse)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid card details|[ApiResponse](#schemaapiresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal server error|[ApiResponse](#schemaapiresponse)|

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "message": "Usages retrieved successfully",
  "data": {
    "usages": 5,
    "usages_today": 2,
    "usage_left_today": 3
  }
}
```

</TabItem>

<TabItem value="400 Response" label="400 Response">

```json
{
  "status": 400,
  "message": "The given card does not exist",
  "data": null
}
```

</TabItem>

<TabItem value="500 Response" label="500 Response">

```json
{
  "status": 500,
  "message": "Internal server error",
  "data": null
}
```

</TabItem>

</Tabs>

## Schemas

<h3 id="tocS_ApiResponse">ApiResponse</h3>
<!-- backwards compatibility -->
<a id="schemaapiresponse"></a>
<a id="schema_ApiResponse"></a>
<a id="tocSapiresponse"></a>
<a id="tocsapiresponse"></a>

```json
{
  "status": 200,
  "message": "OK",
  "data": {}
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|status|integer|false|none|The HTTP status code of the response|
|message|string|false|none|A message describing the outcome of the request|
|data|object¦null|false|none|Additional data (if applicable)|

<h3 id="tocS_WithdrawMealDTO">WithdrawMealDTO</h3>
<!-- backwards compatibility -->
<a id="schemawithdrawmealdto"></a>
<a id="schema_WithdrawMealDTO"></a>
<a id="tocSwithdrawmealdto"></a>
<a id="tocswithdrawmealdto"></a>

```json
{
  "fridge_id": 1,
  "card": {
    "number": "1234567890",
    "security_code": "1234"
  },
  "meals": [
    {
      "id": 42
    }
  ]
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|fridge_id|integer|false|none|The ID of the fridge from which to withdraw meals|
|card|[CardDTO](#schemacarddto)|false|none|none|
|meals|[[MealDTO](#schemamealdto)]|false|none|A list of meals to withdraw|

<h3 id="tocS_CardDTO">CardDTO</h3>
<!-- backwards compatibility -->
<a id="schemacarddto"></a>
<a id="schema_CardDTO"></a>
<a id="tocScarddto"></a>
<a id="tocscarddto"></a>

```json
{
  "number": "1234567890",
  "security_code": "1234"
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|number|string|false|none|The card number|
|security_code|string|false|none|The card's security code|

<h3 id="tocS_MealDTO">MealDTO</h3>
<!-- backwards compatibility -->
<a id="schemamealdto"></a>
<a id="schema_MealDTO"></a>
<a id="tocSmealdto"></a>
<a id="tocsmealdto"></a>

```json
{
  "id": 42
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer|false|none|The ID of the meal to withdraw|

