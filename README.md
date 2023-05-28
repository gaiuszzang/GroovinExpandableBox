## GroovinCollapsingToolBar
[![Release](https://jitpack.io/v/gaiuszzang/GroovinExpandableBox.svg)](https://jitpack.io/#gaiuszzang/GroovinExpandableBox)  
This library offers a Box Composable that can be expanded/reduced through up/down swipe gestures.

|                                                    MusicPlayer Sample                                                    |                                                    Article Page Sample                                                     |
|:------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------:|
| ![music_sample](https://github.com/gaiuszzang/GroovinExpandableBox/assets/15318053/5cca2871-b694-4002-955a-26f51385c0b6) | ![article_sample](https://github.com/gaiuszzang/GroovinExpandableBox/assets/15318053/78bdc12b-6884-4d4b-9470-76440474d461) |


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
for old gradle version, Add below codes to **your project**'s `build.gradle`.
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation 'com.github.gaiuszzang:GroovinExpandableBox:x.x.x'
}
```


## Usage
### ExpandableBox
`ExpandableBox` is Expandable Layout based on Box through up/down swipe gestures.
```kotlin
ExpandableBox(
    modifier = modifier,
    swipeableState = rememberSwipeableState(initialValue = ExpandableBoxState.FOLD),
    isDownDirection = true,
    isHideable = false,
    foldHeight = 200.dp
) {
    Content(...)   //Contents Composable
}
```
There are 4 arguments to be aware of use.
 - `swipeableState` : ExpandableBox needs SwipeableState<ExpandableBoxState> for store and use its status. See the section below for details.
 - `isDownDirection` : In true case, It expands with a swipe up gesture and fold with a swipe down gesture. `default = true`
 - `isHideable` : In true case, User try to fold one more time in the folded state, it will be hidden. `default = false`
 - `foldHeight` : Define the Fold Status Height. `Mandatory`


#### SwipeableState<ExpandableBoxState>
ExpandableBox needs `SwipeableState<ExpandableBoxState>` instance for store and use its status.
```kotlin
val swipeableState = rememberSwipeableState(initialValue = ExpandableBoxState.FOLD)
```
One of the values below must be set as the initial value.
- `ExpandableBoxState.HIDE` : Begin with no visible.
- `ExpandableBoxState.FOLD` : Begin with Folding Status.
- `ExpandableBoxState.EXPAND` : Begin with Expanding Status.



#### ExpandableBoxScope
A `ExpandableBoxScope` provides a scope with attributes for the content of ExpandableBox.
- `progress: Float` : Progress value(0 ~ 1f) between `HIDE` to `FOLD`, or `FOLD` to `EXPAND`.
- `progressState: ExpandableBoxState` : It means the current state and provide one of the following States : `HIDE, HIDING, FOLD, FOLDING, EXPAND`
- `completedState: ExpandableBoxState` : It means the state that swipable action is completed, and provide one of the following States : `HIDE, FOLD, EXPAND`

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
