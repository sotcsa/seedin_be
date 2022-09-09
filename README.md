# SEED - a web3 career hub

## Swagger
http://localhost:17641/swagger-ui/index.html

## DB design
[DRAFT] https://dbdiagram.io/d/63158b620911f91ba535094c

## Backend-Frontend Authentication

Frontend handles user's wallet (MetaMask, NEAR wallet).
Backend needs to authenticate frontend users.

For that we have implemented a one-click-login solution (based on [this tutorial](https://www.toptal.com/ethereum/one-click-login-flows-a-metamask-tutorial))

### Login with MetaMask Overview

![Alt text](https://uploads.toptal.io/blog/image/125792/toptal-blog-image-1522395353253-70fb1c40e9527154c2774507b63eac63.png "Optional title")

### Auth endpoints
#### GET /auth/nonce?address={ethereum_account_address}
E.g.
http://localhost:17641/auth/nonce?address=0x6C542189c4Dbc4d5D7b4845388b8D561f9e2e96B

Response: raw string, the nonce assigned to the {ethereum_account_address}

Backend generates a UUID string to be used in signed message. Stores in db.


#### POST /auth/eth
Request (example):
```shell
{
  "publicAddress": "0x6C542189c4Dbc4d5D7b4845388b8D561f9e2e96B",
  "signature": "0xdce78b657f3c9a61423ca00fd636140e1acba25790b69585d5c26f7daa300cbc2023a9c342c2348718f634549df9a606383ae2bc419df76314c7946c800dce6f1c"
 }
```

E.g.
```shell
curl -X POST 'http://localhost:17641/auth/eth' \
 -H 'Content-Type: application/json' \
 --data-raw '{
   "publicAddress": "0x6C542189c4Dbc4d5D7b4845388b8D561f9e2e96B",
   "signature": "0xdce78b657f3c9a61423ca00fd636140e1acba25790b69585d5c26f7daa300cbc2023a9c342c2348718f634549df9a606383ae2bc419df76314c7946c800dce6f1c"
 }'
```

Response: JSON with jwt field (Json Web Token) 
```shell
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIweDZDNTQyMTg5YzREYmM0ZDVEN2I0ODQ1Mzg4YjhENTYxZjllMmU5NkIiLCJleHAiOjE2NjI0MTk2MzMsImlhdCI6MTY2MjQxNjAzM30.qgqSMfTr5SgGhpKKryMb98tNxHFENvwyGd8wbTPibwY"
}
```

Frontend needs to construct the message (see bellow) and ask user to sign this message with MetaMask.

Here is an example node code:
```javascript
let msg = 'Please sign this message for authentication on the Career Portal.\nYour special nonce: ' + nonce
s = web3.eth.accounts.sign(msg, privKey);
```

Backend does the signed message validation, and generates for the frontend  a JWT with 1 hour expiry time

