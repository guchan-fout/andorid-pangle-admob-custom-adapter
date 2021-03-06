# AdMob Custom Event Adapter for Pangle

> Please set [Admob](https://developers.google.cn/admob/android/quick-start) in your app first.

* [Setup Pangle Platform](#setup-pangle)
* [Add Pangle to AdMob's mediation](#add-pangle)
* [Initialize Pangle SDK and Adapter](#import-pangle)

<a name="setup-pangle"></a>
## Setup Pangle Platform
### Create a Pangle account

- Please create a [Pangle account](https://ad.oceanengine.com/union/media/login) if you do no have one.


### Create an application and placements in Pangle

- Click `Apps` -> `+ Add App` to create a app for mediation.

<img src="./pics/create-app-home.png" alt="drawing" width="400"/>

<img src="./pics/create-app.png" alt="drawing" width="300"/>

<a name="app-id"></a>
- You will get an app with its `app ID`.

<img src="./pics/app-id.png" alt="drawing" width="400"/>



### Create Ad Placement
- Click `Ad Placements` -> `+ Add Ad Placement` to create the placement for mediation.

<img src="./pics/create-placement.png" alt="drawing" width="400"/>

- Select the ad's type for your app and finish the create.

<img src="./pics/ad-type.png" alt="drawing" width="400"/>

<a name="placementID"></a>
- You will get a placement with its `placement ID`.

<img src="./pics/placement-id.png" alt="drawing" width="400"/>


<a name="add-pangle"></a>
## Add Pangle to AdMob's mediation

### Create mediation

- Click `Mediation` -> `CREATE MEDIATION GROUP` to create a mediation group.


<img src="./pics/add-mediation.png" alt="drawing" width="400"/>


- Select the same ad format which created on Pangle side.

<img src="./pics/ad-format.png" alt="drawing" width="400"/>


- After select the ad unit you created on AdMob which you want to embed mediation, click `ADD CUSTOM EVENT` to set with Pangle.


<img src="./pics/add-custom-event.png" alt="drawing" width="400"/>


- Add adapter's **package + class name** to Class Name.
  - **Class Name**: [package].[adaptername], for example,`com.bytedance.pangle.admob.adapter.demo.pangle.adapter.AdmobRewardVideoAdapter`

  <img src="./pics/class-name.png" alt="drawing" width="400"/>

- Add `{"placementID":"your slot ID"}` to Parameter.
    - **Parameter**: Add {"placementID":"[your placement ID on Pangle](#placementID)"} to Parameter , for example,`{"placementID":"1234567"}`


<img src="./pics/mediation-param.png" alt="drawing" width="400"/>


**Please make sure to use JSON to set Parameter. Or you need to customize adapter yourself.**

<a name="import-pangle"></a>
## Initialize Pangle SDK and Adapter

### Initialize Pangle SDK
- Please refer to [Integrate Pangle SDK](https://www.pangleglobal.com/help/doc/6034a663511c57004360ff0f)
and [Initialize Pangle SDK](https://www.pangleglobal.com/help/doc/6034a671bc3f04003eef4f9f) to intergrate Pangle SDK in your application.

 * **Please use [app ID](#app-id) to initialize Pangle SDK.**

### Embed Pangle Adapters
- Click `SDK Integration` -> `SDK download`, you can find adapters for different ad formats from your Pangle platform,  embed them in your app and they can be used with no code changes. Also you can customize it for your use case.
<br>
<img src="./pics/mediation.png" alt="drawing" width="400"/>
<br>
<img src="./pics/adapter-download.png" alt="drawing" width="400"/>

- You can find simple use cases from [Demo](../AndroidDemo).
