---
title: Authentication API v1.0.0
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

API for user authentication and JWT token issuance.

Base URLs:

* <a href="https://fridgebridge.simplecharity.com">https://fridgebridge.simplecharity.com</a>

## `POST /auth/token`

*Issue JWT Token*

Issues a JWT access token for authenticated users.

<h3 id="post__auth_token-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[MailPasswordLoginReqBody](#schemamailpasswordloginreqbody)|true|Login payload with email and password|

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X POST https://fridgebridge.simplecharity.com/auth/token \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json'

```
</TabItem>

<TabItem value="http" label="http">

```http
POST https://fridgebridge.simplecharity.com/auth/token HTTP/1.1
Host: fridgebridge.simplecharity.com
Content-Type: application/json
Accept: application/json

```
</TabItem>

<TabItem value="javascript" label="javascript">

```javascript
const inputBody = '{
  "mail": "user@example.com",
  "password": "securepassword123"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'application/json'
};

fetch('https://fridgebridge.simplecharity.com/auth/token',
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

result = RestClient.post 'https://fridgebridge.simplecharity.com/auth/token',
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

r = requests.post('https://fridgebridge.simplecharity.com/auth/token', headers = headers)

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
    $response = $client->request('POST','https://fridgebridge.simplecharity.com/auth/token', array(
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
URL obj = new URL("https://fridgebridge.simplecharity.com/auth/token");
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
    req, err := http.NewRequest("POST", "https://fridgebridge.simplecharity.com/auth/token", data)
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
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|JWT token issued successfully.|[ApiResponseWithToken](#schemaapiresponsewithtoken)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid request body or missing parameters.|[ApiResponse](#schemaapiresponse)|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|Unauthorized access.|[ApiResponse](#schemaapiresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal server error.|[ApiResponse](#schemaapiresponse)|

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "data": {
    "access-token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtYWlsIjoidXNlckBleGFtcGxlLmNvbSIsIm93bmVyX2lkIjoiMTIzNCIsInR5cGUiOiJsZWdhbCIsImV4cCI6MTYzMDYyMzAwMH0.abcdefg12345"
  }
}
```

</TabItem>

</Tabs>

## Schemas

<h3 id="tocS_MailPasswordLoginReqBody">MailPasswordLoginReqBody</h3>
<!-- backwards compatibility -->
<a id="schemamailpasswordloginreqbody"></a>
<a id="schema_MailPasswordLoginReqBody"></a>
<a id="tocSmailpasswordloginreqbody"></a>
<a id="tocsmailpasswordloginreqbody"></a>

```json
{
  "mail": "user@example.com",
  "password": "securepassword123"
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|mail|string(email)|true|none|none|
|password|string|true|none|none|

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

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|status|integer|false|none|none|
|message|string|false|none|none|

<h3 id="tocS_ApiResponseWithToken">ApiResponseWithToken</h3>
<!-- backwards compatibility -->
<a id="schemaapiresponsewithtoken"></a>
<a id="schema_ApiResponseWithToken"></a>
<a id="tocSapiresponsewithtoken"></a>
<a id="tocsapiresponsewithtoken"></a>

```json
{
  "status": 200,
  "data": {
    "access-token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtYWlsIjoidXNlckBleGFtcGxlLmNvbSIsIm93bmVyX2lkIjoiMTIzNCIsInR5cGUiOiJsZWdhbCIsImV4cCI6MTYzMDYyMzAwMH0.abcdefg12345"
  }
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|status|integer|false|none|none|
|data|object|false|none|none|
|» access-token|string|false|none|none|

