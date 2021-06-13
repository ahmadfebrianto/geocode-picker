# geocode-picker   
  An android app for getting the geocode of a place in Google Maps.

### App Overview 
![](https://media.giphy.com/media/2wZO1lXK783qIVtURG/giphy.gif)

### Prerequisites   
* #### Create Google Maps API key.   
  Refer to this link for the detailed tutorial on how to create a Google MAPS API key:

  > https://developers.google.com/maps/documentation/android-sdk/get-api-key   
  
  
  #### NOTE: 
    Google Maps Platform gives ``$200`` in free usage for Maps, Routes, and Places every month. 
    Do NOT worry, ``$200`` is more than enough for daily use. Refer to this link for the pricing details: 
    > https://cloud.google.com/maps-platform/pricing   
    
    
* #### Place the API key in the project.   
    * Create a ``local.properties`` file in the top level directory (the same directory as the app folder).
    * Create a new variable named ``MAPS_API_KEY`` in the newly create file and insert your API key as the value without **quotes**.

        ```kotlin
        MAPS_API_KEY=your_api_key
        ```
    * Build the app.
