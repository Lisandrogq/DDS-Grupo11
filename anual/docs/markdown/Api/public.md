---
title: FridgeBridge API v1.0.0
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

These are the FridgeBridge API endpoints to interact with our services.

Base URLs:

* <a href="https://fridgebridge.simplecharity.com/">http://fridgebridge.simplecharity.com</a>

## `GET /api/contributors/recognitions`

*Returns a list of employees who meet the minimum score, meal donation count.*

<h3 id="get__api_contributors_recognitions-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|min_points|query|integer|true|Minimum accumulated score required.|
|min_meals|query|integer|true|Minimum number of meal donations made.|
|size|query|integer|false|Maximum number of contributors to include in the query.|

### Code samples

<Tabs>
<TabItem value="shell" label="shell">

```shell
curl -X GET http://localhost:8000/api/contributors/recognitions?min_points=0&min_meals=0 \
  -H 'Accept: application/json'

```
</TabItem>

<TabItem value="http" label="http">

```http
GET http://localhost:8000/api/contributors/recognitions?min_points=0&min_meals=0 HTTP/1.1
Host: localhost:8000
Accept: application/json

```
</TabItem>

<TabItem value="javascript" label="javascript">

```javascript

const headers = {
  'Accept':'application/json'
};

fetch('http://localhost:8000/api/contributors/recognitions?min_points=0&min_meals=0',
{
  method: 'GET',

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
  'Accept' => 'application/json'
}

result = RestClient.get 'http://localhost:8000/api/contributors/recognitions',
  params: {
  'min_points' => 'integer',
'min_meals' => 'integer'
}, headers: headers

p JSON.parse(result)

```
</TabItem>

<TabItem value="python" label="python">

```python
import requests
headers = {
  'Accept': 'application/json'
}

r = requests.get('http://localhost:8000/api/contributors/recognitions', params={
  'min_points': '0',  'min_meals': '0'
}, headers = headers)

print(r.json())

```
</TabItem>

<TabItem value="php" label="php">

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => 'application/json',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8000/api/contributors/recognitions', array(
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
URL obj = new URL("http://localhost:8000/api/contributors/recognitions?min_points=0&min_meals=0");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
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
        "Accept": []string{"application/json"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8000/api/contributors/recognitions", data)
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
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|List of employees who meet the specified criteria.|[ContributorRecognitionResponse](#schemacontributorrecognitionresponse)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Missing required parameter or Invalid input.|[ApiResponse](#schemaapiresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error.|[ApiResponse](#schemaapiresponse)|

### Example responses

<Tabs>
<TabItem value="200 Response" label="200 Response">

```json
{
  "status": 200,
  "message": "OK",
  "data": {
    "individuals": [
      {
        "id": "1",
        "name": "Alberto",
        "surname": "Fernandez",
        "address": "Espana",
        "birth": "1960-05-12",
        "document": "19374892",
        "documentType": "DNI",
        "points": 120,
        "mealDonations": 5
      }
    ],
    "legalEntities": [
      {
        "id": "1",
        "type": "Company",
        "category": "Education",
        "points": 21,
        "mealDonations": 3
      }
    ]
  }
}
```

</TabItem>

<TabItem value="400 Response" label="400 Response">

```json
{
  "status": 400,
  "message": "Missing required parameter: minPoints.",
  "data": {}
}
```

</TabItem>

<TabItem value="500 Response" label="500 Response">

```json
{
  "status": 500,
  "message": "Internal Server Error.",
  "data": {}
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
|status|integer|false|none|HTTP status code of the response.|
|message|string|false|none|Default status message or custom message.|
|data|object|false|none|Additional data relevant to the response.|

<h3 id="tocS_ContributorRecognitionResponse">ContributorRecognitionResponse</h3>
<!-- backwards compatibility -->
<a id="schemacontributorrecognitionresponse"></a>
<a id="schema_ContributorRecognitionResponse"></a>
<a id="tocScontributorrecognitionresponse"></a>
<a id="tocscontributorrecognitionresponse"></a>

```json
{
  "status": 200,
  "message": "OK",
  "data": {
    "individuals": [
      {
        "id": "string",
        "name": "string",
        "surname": "string",
        "address": "string",
        "birth": "2019-08-24",
        "document": "string",
        "documentType": "LC",
        "points": 0,
        "mealDonations": 0
      }
    ],
    "legalEntities": [
      {
        "id": "string",
        "type": "Governmental",
        "category": "HealthCare",
        "points": 0,
        "mealDonations": 0
      }
    ]
  }
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|status|integer|true|none|HTTP status code of the response.|
|message|string|true|none|Default status message or custom message.|
|data|object|true|none|A map of individuals and legal entities that fulfill the constraints.|
|» individuals|[[Individual](#schemaindividual)]|true|none|A list of individuals.|
|» legalEntities|[[LegalEntity](#schemalegalentity)]|true|none|A list of legal entities.|

<h3 id="tocS_Individual">Individual</h3>
<!-- backwards compatibility -->
<a id="schemaindividual"></a>
<a id="schema_Individual"></a>
<a id="tocSindividual"></a>
<a id="tocsindividual"></a>

```json
{
  "id": "string",
  "name": "string",
  "surname": "string",
  "address": "string",
  "birth": "2019-08-24",
  "document": "string",
  "documentType": "LC",
  "points": 0,
  "mealDonations": 0
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|string|true|none|Unique identifier for the individual.|
|name|string|true|none|First name of the individual.|
|surname|string|true|none|Surname of the individual.|
|address|string|true|none|Address of the individual.|
|birth|string(date)|true|none|Birthdate of the individual (YYYY-MM-DD).|
|document|string|true|none|Identification document number of the individual.|
|documentType|string|true|none|Type of the identification document.|
|points|integer|true|none|Total points earned by the individual.|
|mealDonations|integer|false|none|Number of meal donations made by the individual.|

#### Enumerated Values

|Property|Value|
|---|---|
|documentType|LC|
|documentType|LE|
|documentType|DNI|

<h3 id="tocS_LegalEntity">LegalEntity</h3>
<!-- backwards compatibility -->
<a id="schemalegalentity"></a>
<a id="schema_LegalEntity"></a>
<a id="tocSlegalentity"></a>
<a id="tocslegalentity"></a>

```json
{
  "id": "string",
  "type": "Governmental",
  "category": "HealthCare",
  "points": 0,
  "mealDonations": 0
}

```

#### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|string|true|none|Unique identifier for the legal entity.|
|type|string|true|none|Type of the legal entity.|
|category|string|true|none|Category of the legal entity.|
|points|integer|true|none|Total points earned by the legal entity.|
|mealDonations|integer|false|none|Number of meal donations made by the legal entity.|

#### Enumerated Values

|Property|Value|
|---|---|
|type|Governmental|
|type|NGO|
|type|Company|
|type|Institution|
|category|HealthCare|
|category|Education|
|category|Finance|
|category|Technology|
|category|Agriculture|
|category|Hospitality|
|category|Transportation|
|category|Manufacturing|
|category|Retail|

