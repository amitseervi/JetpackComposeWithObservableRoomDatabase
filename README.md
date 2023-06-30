# Radius Agent assignment project

## Summary

Radius is Demo project which uses network calls to fetch list of Facilities and render it on UI
using Jetpaack compose. Each facility item has number of option from which user can select any one
option. This user selection is persisted with help of Room Database and updated on Ui through Room
live update this help maintain single source of truth across application.

## Option Exclusion handling

Further From network api response we get exclusion list which tells us that which options are
mutually exclusive to each others. Currently in the api in each exclusion set we only get two
options exclusive to each other but for scalability purpose if any option selected from exclusion
rule set other options are disabled and not clickable.

## Network Caching

To avoid calling api again and again custom NetworkCache is implemented to store the network
response in file and use it for next api call till TTL expire. this TTL is compared with system
currentTime and file last modified timeStamp which is set when writing this file from network
response. This network Cache interceptor can be used for all type of api with header configuration
which is declared on api declaration using retrofit client.

#### why custom network caching instead of okhttp default caching mechanism?
- This custom network caching class can be used to cache all type of request regardless of what method is used for calling api{POST/GET/etc}. By normal rest api development we should only cache GET method api based on cache-ttl provided from response but sometime we need custom caching logic at client side to handle specific scenarios.

## Libraries used
- Jetpack compose
- Room
- Jetpack compose material3 (Experimental component used)
- Retrofit
- Hilt

## Image
![radius_screenshot.png](https://github.com/amitseervi/radius/blob/main/radius_screenshot.png)

#### Next Steps
- [Fix] release build is not working due to R8 minimisation network response model is not able to populate field from network response to data class properly.
