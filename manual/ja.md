# AdMob Custom Event Adapter for Pangle

> Please set [Admob](https://developers.google.cn/admob/android/quick-start) in your app first.

* [Pangleプラットフォームのセットアップ](#setup-pangle)
* [AdMobのmediationにPangleを追加](#add-pangle)
* [Pangle SDKとAdapterの導入と初期化](#import-pangle)

<a name="setup-pangle"></a>
## Pangleプラットフォームのセットアップ
### Pangleアカウントを作成

- [Pangleアカウント](https://ad.oceanengine.com/union/media/login)をお持ちでない場合は作成してください。


### Pangleでアプリケーションとプレースメントを作成

- `Apps` -> `+ Add App` をクリックして、メディエーション用のアプリを作成します。
<br>
<img src="./pics/create-app-home.png" alt="drawing" width="400"/>
<br>
<img src="./pics/create-app.png" alt="drawing" width="300"/>

<a name="app-id"></a>
- `app ID`が付いたアプリが作成されます。
<br>
<img src="./pics/app-id.png" alt="drawing" width="400"/>



### 広告プレースメントを作成する
- `Ad Placements` -> `+ Add Ad Placement`をクリックして、メディエーション用のアプリに属するプレースメントを作成します。
<br>
<img src="./pics/create-placement.png" alt="drawing" width="400"/>

- 広告の種類を選択して、作成を完了します
<br>
<img src="./pics/ad-type.png" alt="drawing" width="400"/>

<a name="placementID"></a>
- `placement ID`付きのプレースメントが作成されます。
<br>
<img src="./pics/placement-id.png" alt="drawing" width="400"/>


<a name="add-pangle"></a>

## AdMobのメディエーションにPangleを追加

### メディエーションを作成

- `Mediation` -> `CREATE MEDIATION GROUP` をクリックして、メディエーショングループを作成します。
<br>
<img src="./pics/add-mediation.png" alt="drawing" width="400"/>


- Pangle側で作成したものと同じ広告フォーマットを選択します。
<br>
<img src="./pics/ad-format.png" alt="drawing" width="400"/>


- メディエーション用のAdmob広告ユニットを選択したら、`ADD CUSTOM EVENT`をクリックしてPangleで設定します。
<br>
<img src="./pics/add-custom-event.png" alt="drawing" width="400"/>


- Adapterの`package name + class nameを`をメディエーション上の`Class Name`枠に追加します。
  - **Class Name**: [package].[adapter], 例：`com.bytedance.pangle.admob.adapter.demo.pangle.adapter.AdmobRewardVideoAdapter`
  <br>
  <img src="./pics/class-name.png" alt="drawing" width="400"/>

- `{"placementID":"your slotID"}`を`Parameter`枠に追加します。

     - **Parameter**: {"placementID":"[your placement ID on Pangle](#placementID)"}のJSON形式, 例：`{"placementID": "1234567"}`
  <br>
  <img src="./pics/mediation-param.png" alt="drawing" width="400"/>


  **必ずJSON形式でパラメータを設定してください。または、アダプターを自分でカスタマイズする必要があります。**

<a name="import-pangle"></a>
## Pangle SDKとAdapterの導入と初期化

### Pangle SDKの導入と初期化
- [SDKの導入](https://www.pangleglobal.com/help/doc/6034a663511c57004360ff0f)
と [SDKの初期化](https://www.pangleglobal.com/help/doc/6034a671bc3f04003eef4f9f) に従ってPangle SDKのインテグレーションを完了します。

 * **[app ID](#app-id)を使用してPangleSDKを初期化してください。**


### Pangle Adaptersの導入
- Pangleプラットフォームから、`SDK Integration` -> `SDK download`をクリックすると、各広告フォーマット用のアダプターをダウンロードでき、アダプターファイルをアプリに導入すればコードを変更せずに使用できます。また、ユースケースに合わせてカスタマイズすることもできます。
<br>
<img src="./pics/mediation.png" alt="drawing" width="400"/>
<br>
<img src="./pics/adapter-download.png" alt="drawing" width="400"/>


- [Demo](../AndroidDemo)から簡単な使用例を確認できます。
