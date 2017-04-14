# ButtonProgressBar
[![](https://jitpack.io/v/ishaan1995/ButtonProgressBar.svg)](https://jitpack.io/#ishaan1995/ButtonProgressBar)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ButtonProgressBar-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5482)

![](https://raw.githubusercontent.com/ishaan1995/ButtonProgressBar/master/output_F2Ryon.gif "Concept Animation")

## To get this Git project into your build:
#### 1.Add this in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 2.Add this dependency in your app level build.gradle:
```
dependencies {
  compile 'com.github.ishaan1995:ButtonProgressBar:1.0'
}
```

## Usage:
#### 1.In your layout XML file:
```xml
<github.ishaan.buttonprogressbar.ButtonProgressBar
        android:id="@+id/bpb_main"
        app:text="Upload"
        app:textColor="@android:color/white"
        app:type="indeterminate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

#### 2.In your class file:
```java
final ButtonProgressBar bar = (ButtonProgressBar) findViewById(R.id.bpb_main);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.startLoader();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bar.stopLoader();
                    }
                }, 5000);
            }
        });
```

Inspiration from [Dribbble](https://dribbble.com/shots/2551579-Download-Button)

[iOS version](https://github.com/thePsguy/ButtonProgressBar-iOS) available!

License
=======

    Copyright 2017 Ishaan Kakkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
