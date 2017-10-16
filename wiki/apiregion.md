Setting API Region
======================

Some of our infrastructure live in isolated environments. The Lenddo Onboarding SDK accepts a configuration of the UIHelper object that defines the api the SDK will be talking to. The SDK ensures that only alphabet characters exist within the region specification. This prevents attempts to override the URL by shunting everything else into a path or hash.

*Sample code:*

```java
helper = new UIHelper(this, this);
// Set API Region to Korea
helper.setApiRegion("kr");
```


