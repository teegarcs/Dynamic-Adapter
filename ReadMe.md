## Introduction to Dynamic Adapter

#### Overview 
Dynamic Adapter replaces the need define and write a custom Adapter for each screen that you need to build a RecyclerView. Its purpose is to be the only adapter that you need within your application. 

Dynamic Adapter is strongly tied to the use of DataBinding. While you are not required to use DataBinding, you will get more use out of the tool if you leverage DataBinding features. 


#### Using Dynamic Adapter

After importing the Dynamic Adapter framework into your project, the only thing you need to do is set the Adapter to your RecyclerView

###### DynamicAdapter

``` 
val adapter = DynamicAdapter()
binding.sampleRecyclerView.adapter = adapter
// observe the activity lifecycle, this is used for the adapter lifecycle to supplement the view
lifecycle.addObserver(adapter)
```

Note that `DynamicAdapter` has a base class of ListAdapter, so it has Diff Util and a default, empty list defined. 

###### DynamicModel
`DynamicModel` is the abstract base class that must be used to set your Item to the DynamicAdapter. 

```
/**
 * Model to be implemented by any class that wishes to show data on the DynamicAdapter.
 *
 * This provides a lifecycle owner so that LiveData and DataBinding can be used. The Lifecycle
 * is managed by the adapter based on the status of the view being attached to the recyclerview or
 * not
 *
 */
abstract class DynamicModel() : LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    /**
     * Called during the onBind of a ViewHolder. The Super will take care of binding the
     * LifecycleOwner and variables defined with the generic naming patterns. Override this function
     * if you need to perform custom actions within the bind, or if you have variable names
     * that are not listed below:
     *
     * <variable
     * name="model"
     * type="com.teegarcs.dynamicadapter.DynamicModel" />
     *
     * <variable
     * name="modelAction"
     * type="com.teegarcs.dynamicadapter.DynamicModelActionCallback" />
     *
     * @param binding - the ViewBinding being bound to this model.
     */
    open fun bindVariables(binding: ViewDataBinding) {
        binding.lifecycleOwner = this
        binding.setVariable(BR.model, this)
        binding.setVariable(BR.adapterAction, getModelActionCallback())
    }


    /**
     * Layout Resource ID for the layout this model will inflate.
     *
     * @return Layout Resource ID
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Get the Lifecycle for the Model.
     *
     * @return Lifecycle
     */
    final override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    /**
     * DynamicModelActionCallback - optional callback for actions that need to be passed up
     * to a fragment, activity, or ViewModel
     */
    open fun getModelActionCallback(): DynamicModelActionCallback? {
        return null
    }


    /**
     * Optional Resource ID that can be used for section headers and sticky headers.
     * This will be ignored unless you set the DynamicSectionHeader to your RecyclerView as an Item Decorator
     *
     * Note that this function is not called if the DynamicSectionHeader is not set as an Item Decorator on your RecyclerView
     */
    @LayoutRes
    open fun getHeaderLayoutId(): Int? = null

    /**
     * Override this function if you want to set a specific matcher to be used for indicating
     * if this is part of a specific section or not. If you want to do something with text,
     * simply make each section implement, grab the text and add .hashCode()
     *
     * Note that this function is not called if the DynamicSectionHeader is not set as an Item Decorator on your RecyclerView
     * 
     *  override fun sectionMatcher(): Int {
     *      return body.substring(0, 1).hashCode()
     *  }
     */
    open fun sectionMatcher(): Int {
        return getLayoutId()
    }

    /**
     * This function gets called right before the header is drawn on the screen.
     * Find views, set titles, images, colors, etc here.
     *
     * Note that this function is not called if the DynamicSectionHeader is not set as an Item Decorator on your RecyclerView
     *  override fun bindHeaderLayout(view: View) {
     *      super.bindHeaderLayout(view)
     *      view.findViewById<TextView>(R.id.list_item_section_text).text = body.substring(0, 1)
     *   }
     */
    open fun bindHeaderLayout(view: View) {}
}

```

###### Using The DynamicModel
A Recycler Item that implements only the required functions for the Dynamic Model, would look like this: 

```
class ImageTextModel(val imageUrl: String, val body: String) : DynamicModel() {
    override fun getLayoutId() = R.layout.image_text_layout
}

```

At times, you need to interact with the raw view of a RecyclerItem. To do so, simply override the bindVariables function like below. Note, you should be sure to implement the super, otherwise your view will not be bound for DataBinding. 

```
override fun bindVariables(binding: ViewDataBinding) {
        super.bindVariables(binding)
        (binding as? YourLayoutBinding)?.let {
            // do something with the view. 
        }
    }
```

###### Action Listeners 
If you wish to pass an event up from your recycler item to your ViewModel, Activity, or Fragment, you can do so by passing in an interface callback to be triggered whenever an action happens in your model such as a button click. In the sample below, we also provide the interface in the `getModelActionCallback` function which supplies this action to your DataBound Layout file: 

```
class ImageTextWithButtonsModel(
    val imageUrl: String,
    val body: String,
    private val actions: ImageTextButtonActions
) : DynamicModel() {

    override fun getLayoutId() = R.layout.image_text_with_buttons_layout
    override fun getModelActionCallback() = actions

  }

interface ImageTextButtonActions : DynamicModelActionCallback {

    // some actions to perform on the ViewModel
}
```

Just be sure to provide the correct Data Variables in your XML: 

```
<data>
        <!-- To Automatically receive the model here, it must be named as below.
        A custom name can be provided but bindVariables() would need to be overridden
        in your model class-->
        <variable
            name="model"
            type="com.teegarcs.dynamicadapterexample.adapter_models.ImageTextWithButtonsModel" />

        <!-- To Automatically receive the adapterAction here, it must be named as below.
        A custom name can be provided but bindVariables() would need to be overridden
        in your model class-->
        <variable
            name="adapterAction"
            type="com.teegarcs.dynamicadapterexample.adapter_models.ImageTextButtonActions" />
    </data>
    
```

###### Dynamic Model LifeCycles 

The DynamicModel carries with it a `LifeCycleOwner` exposing the lifecycle methods of `onCreate`, `onResume`, `onPause`, and `onDestroy`. You can use this for subscriptions to LiveData and for managing Coroutines from within your Model. These lifecycle methods work as you would expect, with `onResume` and `onPause` firing based on whether the user can see your item in the RecyclerView or not. 

Below is a sample of a model that leverages the lifecycle of the model to start and stop a coroutine based on whether the user can see the view in the RecyclerView or not. 

```
class TimerLabelModel : DynamicModel() {

    val labelLD = MutableLiveData<String>().apply { value = "Shown for 0 seconds" }
    private var counter: Int = 0

    override fun getLayoutId() = R.layout.timer_label_layout

    init {
        //keeps time of how long the user has been looking at the view.
        // Only runs when the view is seen by the user
        lifecycleScope.launchWhenResumed {
            while (isActive) {
                delay(1000)
                counter += 1
                labelLD.value = "Shown for $counter seconds"
            }
        }
    }
}

```


##### Dynamic Section Header 

The Dynamic Adapter framework also comes with support for section headers that can scroll or be sticky. These headers are customizable on a per section basis - with each one having the ability to provide their own custom section header. To do this, simply add the DynamicSectionHeader as an item decorator to your RecyclerView: 

```
 // note that the boolean value indicates we want sticky or not
 binding.sampleRecyclerView.addItemDecoration(DynamicSectionHeader(true))
```

Next, in your model supply the layout id to use for the Header by overriding and providing this here: 

```
 override fun getHeaderLayoutId() = R.layout.sample_section_header_one
```
By default, the getHeaderLayoutId returns null, and this function is ignored if you do not set the DynamicSectionHeader as an ItemDecorator. 


Next you can interact with your header by overriding the bindHeaderLayout function: 

```
override fun bindHeaderLayout(view: View) {
     super.bindHeaderLayout(view)
     view.findViewById<TextView>(R.id.list_item_section_text).text = "Please Select One"
}
```

###### Defining a Section
Sections should be grouped together to ensure they are positioned and displayed correctly. If they are not, section headers will display for items that are different from the section prior to it in the list. 

Sections are defined using the Section Matcher function on the DynamicModel. This allows you to implement your own custom section identifier for the DynamicSectionHeader to use as a matcher. 

```
override fun sectionMatcher(): Int {
   return title.substring(0, 1).hashCode()
}
```
Do note that sections that have the same matcher returned but different section header layouts, will be considered different sections. 


