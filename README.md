# Steps
 - Test the methods defined in the server to make sure you understand it
 - Look at the model defined in the server, if there are more than 4 parameters in the model, go to step 4 then step 3
 - Change the data type of /data/TheItem fields to match the defined model 
 - Modify the /data/TheItem to match the field numbers in the defined model. In case of adding new fields you will need to display those fields in either res/layout/item and/or res/layout/fragment_detail on your preference or find the usages with right-click find usage of the fileds you want to remove and try to logically remove all references of them until the app compiles
 - Update the /data/TheItem serialized name annotations to match the json specification 
 - Update the Url and port from data/networking/TheNetworkingProvider and from /data/websocket/TheWebSocketProvider to match the specifications
 - In the networking/TheNetworkingProvider update the function annotations to match the given endpoints then the list should appear on the screen adding should be functional also local caching
 - If the websocket connection requires a specific endpoint, in the networking/TheWebSocketProvider, in the URL, after the port add the endpoint with /endpoint_name
 - If the websocket connection doesn't work we will switch to the time based reload method. In view/list/TheListViewModel, in the innit block, comment the websocket call and uncomment the manual poll call
