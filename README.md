## GroovinExpandableBox
[![Release](https://jitpack.io/v/io.groovin/GroovinExpandableBox.svg)](https://jitpack.io/#io.groovin/GroovinExpandableBox)  
This library offers a Box Composable that can be expanded/reduced through up/down swipe gestures.

|                                                                    MusicPlayer Sample                                                                     |                                                                    Article Page Sample                                                                    |                                                                    Map Sample                                                                     |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/gaiuszzang/GroovinExpandableBox/assets/15318053/66df4c2e-c4f6-498b-8522-61497cef70a4" alt="MusicPlayerSample" width="240px"> | <img src="https://github.com/gaiuszzang/GroovinExpandableBox/assets/15318053/78bdc12b-6884-4d4b-9470-76440474d461" alt="ArticlePageSample" width="240px"> | <img src="https://github.com/gaiuszzang/GroovinExpandableBox/assets/15318053/233f2b70-f706-45fc-89b4-d92227b6467e" alt="MapSample" width="240px"> |


## Including in your project
### Gradle
Add below codes to `settings.gradle`.
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation 'io.groovin:GroovinExpandableBox:x.x.x'
}
```


## Usage
### ExpandableBox
`ExpandableBox` is Expandable Layout based on Box through up/down swipe gestures.
```kotlin
ExpandableBox(
    modifier = modifier,
    expandableBoxState = rememberExpandableBoxState(initialValue = ExpandableBoxStateValue.Fold),
    swipeDirection = ExpandableBoxSwipeDirection.SwipeUpToExpand,
    foldHeight = 100.dp,
    halfExpandHeight = 300.dp,
    swipeGestureEnabled = true,
    nestedScrollEnabled = true
) {
    Content(...)   //Contents Composable
}
```
There are 6 arguments to be aware of use.
 - `expandableBoxState` : ExpandableBox needs ExpandableBoxState for store and use its status. See the section below for details.
 - `swipeDirection` : You can set the Swipe direction for expanding this. `default = SwipeUpToExpand`
 - `foldHeight` : Define the Minimized Height. `Mandatory`
 - `halfExpandHeight` : Define the Half Expanded Height If you want to use Half Expanded State. If not defined, half expanded state is not used. `Optional`
 - `expandHeight` : Define the Fully Expanded Height. The default is `Dp.Unspecified`, which sets it to the parent's maximum height. `Optional`
 - `swipeGestureEnabled` : You can disable swipe gesture. `default = true`
 - `nestedScrollEnabled` : You can enable nested scrolling to allowing seamless swipe gesture with scrollable composable like Column or LazyColumn. `default = true`


#### ExpandableBoxState
ExpandableBox needs `ExpandableBoxState` instance for store and use its status.
```kotlin
val expandableBoxState = rememberExpandableBoxState(initialValue = ExpandableBoxStateValue.Fold)
```
One of the values below must be set as the initial value.
- `ExpandableBoxStateValue.Fold` : Begin with Minimized state.
- `ExpandableBoxStateValue.HalfExpand` : Begin with Half Expanded state. Note that halfExpandHeight parameter in ExpandableBox should be defined to specific value for use this.
- `ExpandableBoxStateValue.Expand` : Begin with Fully Expanded state.

ExpandableBoxState exposes following parameters.
- `completedValue` : Indicates that latest completion State : `Fold`, `HalfExpand`, `Expand`
- `progressValue` : Indicates that current Progress State : `Fold`, `Folding`, `HalfExpanding`, `Expanding`, `Expand`
- `offset` : Indicates that current height pixel.
Note that `Folding`, `HalfExpand` State is only available when only Half Expand state is used.

#### ExpandableBoxScope
A `ExpandableBoxScope` provides a scope with attributes for the content of ExpandableBox.
- `progress: Float` : Progress value(0 ~ 1f) between `Fold` to `Expand`(in use Half Expand state case, `Fold` to `HalfExpand`, or `HalfExpand` to `Expand`).
- `completedState: ExpandableBoxStateValue` : It is same with ExpandableBoxState.completedValue
- `progressState: ExpandableBoxStateValue` : It is same with ExpandableBoxState.progressValue

also, ExpandableBoxScope inheriting BoxScope, it can be used the same as BoxScope.

## License
```xml
Copyright 2023 gaiuszzang (Mincheol Shin)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
