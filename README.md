# pangle-admob-custom-adapter

> Please set [Admob](https://developers.google.cn/admob/android/quick-start) in your app first

## Setup Pangle
### Create App

- Add App

<img src="./pics/create-app.png" alt="drawing" width="400"/>

- Set App

<img src="./pics/new-app.png" alt="drawing" width="400"/>



### Create Ad Placement

<img src="./pics/new-pangle-slot.png" alt="drawing" width="400"/>



## Add Pangle to AdMob's mediation

<img src="./pics/mediation-param.png" alt="drawing" width="400"/>


- Add adapter's [packagename].[adaptername] to Class Name.

- Add {"slotID": "your slot ID"} to Parameter.

**Please make sure to use JSON to set Parameter. Or you need to customize adapter yourself.**
