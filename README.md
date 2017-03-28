# ButtonProgressBar
[![](https://jitpack.io/v/ishaan1995/ButtonProgressBar.svg)](https://jitpack.io/#ishaan1995/ButtonProgressBar)
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

A Button ProgressBar library, inspiration from [Dribble](https://dribbble.com/shots/2551579-Download-Button)
