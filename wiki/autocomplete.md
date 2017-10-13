AutoComplete View for Android======================The Lenddo Onboarding SDK has provided an Android AutoCompleteTextView that gets information online. Upon entering 3 or more characters the sdk will download To use the AutoComplete View just follow the steps below:1.  Add the view in your xml layout:

```xml <com.lenddo.sdk.widget.OnlineAutoCompleteTextView
    android:id="@+id/editTextUniversity"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />

 <com.lenddo.sdk.widget.OnlineAutoCompleteTextView
    android:tag="name_of_employer"
    android:id="@+id/editTextNameOfEmployer"
    android:layout_width="match_parent"
```

2.  In you screens Activity:

```java
OnlineAutoCompleteTextView  university = (OnlineAutoCompleteTextView) findViewById(R.id.editTextUniversity);
university.region = AutoCompleteQuery.REGION_PH;
university.collection = AutoCompleteQuery.COLLECTION_UNIVERSITIES;
university.version = "0";

OnlineAutoCompleteTextView nameOfEmployer = (OnlineAutoCompleteTextView) findViewById(R.id.editTextNameOfEmployer);
nameOfEmployer.region = AutoCompleteQuery.REGION_PH;
nameOfEmployer.collection = AutoCompleteQuery.COLLECTION_EMPLOYERS;
nameOfEmployer.version = "0";
```

**Parameters:**

*Region* will filter the search based on the location.

*Collection* specifies the category filter.

*Version* specifies the autocomplete version list. For now just use "0".

