package com.teegarcs.dynamicadapterexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teegarcs.dynamicadapter.DynamicModel
import com.teegarcs.dynamicadapterexample.adapter_models.*
import com.teegarcs.dynamicadapterexample.adapter_models.carousel.HorizontalCarouselModel
import com.teegarcs.dynamicadapterexample.util.SingleLiveData

class MainViewModel : ViewModel(), ImageTextButtonActions, CheckBoxAction {
    private val _listContent = MutableLiveData<List<DynamicModel>>().apply { value = listOf() }
    val listContent: LiveData<List<DynamicModel>> = _listContent
    val showToast = SingleLiveData<String>()

    init {
        val listData = mutableListOf<DynamicModel>()

        // This item demos how each recycler item has a lifecycle based on if the view is shown or not
        listData.add(TimerLabelModel())

        // these items demonstrate how models can be updated without notifying the adapter
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))

        // shows how we can build complex UI with very little code in the model class
        listData.add(
            ImageTextModel(
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1c/04/57/d9/marriott-resort-virginia.jpg?w=900&h=-1&s=1",
                "As travellers worldwide dream of escaping on holiday, Marriott Bonvoy is expanding in some of the most desirable leisure destinations across Europe and The United Arab Emirates."
            )
        )

        // demonstrates how to add actions to the model class and remove the items from the list very easily
        listData.add(
            ImageTextWithButtonsModel(
                "https://bsmedia.business-standard.com/_media/bs/img/article/2020-07/25/full/1595618904-5908.jpg",
                "Google in November this year revised its storage policies for Gmail, Drive and Photos. The updated policies will not take effect until June 1, 2021.",
                this
            )
        )

        // shows how we can build complex UI with very little code in the model class
        listData.add(
            ImageTextModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMm5N6vcL6IGP6-MW9DTYxDINtYwcLhCpfGA&usqp=CAU",
                "Learn at your own pace, with flexible and personalised training courses designed to build your confidence and help you thrive."
            )
        )

        // demonstrates how to add actions to the model class and remove the items from the list very easily
        listData.add(
            ImageTextWithButtonsModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkRMaxRL_zHS6Qz20in4krjpZ077yGYSzmdQ&usqp=CAU",
                "Dogs are known to surf if their owner loves to surf. Dogs are very keen to staying dry.",
                this
            )
        )

        // Horizontal Carousel with nested recycler view and adapter. This one has images in different order from one below.
        listData.add(
            HorizontalCarouselModel(
                listOf(
                    "https://images.freeimages.com/images/small-previews/adf/sun-burst-1478549.jpg",
                    "https://www.everypixel.com/i/free_1.jpg",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM8xBYMab0g-XvZCPneZ7xkTqupmgRxFOOwA&usqp=CAU",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkRMaxRL_zHS6Qz20in4krjpZ077yGYSzmdQ&usqp=CAU",
                    "https://cdn.pixabay.com/user/2012/04/01/00-18-38-212_250x250.png"
                )
            )
        )

        // demonstrates how to add actions to the model class and remove the items from the list very easily
        listData.add(
            ImageTextWithButtonsModel(
                "https://images.freeimages.com/images/small-previews/adf/sun-burst-1478549.jpg",
                "The sun peaking through a cloud covered sky.",
                this
            )
        )

        // demonstrates how to add actions to the model class and remove the items from the list very easily
        listData.add(
            ImageTextWithButtonsModel(
                "https://cdn.pixabay.com/user/2012/04/01/00-18-38-212_250x250.png",
                "A wonderful cartoon of a fish with many different colors.",
                this
            )
        )

        // shows how we can build complex UI with very little code in the model class
        listData.add(
            ImageTextModel(
                "https://www.everypixel.com/i/free_1.jpg",
                "A majestic bird of prey"
            )
        )

        // demonstrates how to add actions to the model class and remove the items from the list very easily
        listData.add(
            ImageTextWithButtonsModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM8xBYMab0g-XvZCPneZ7xkTqupmgRxFOOwA&usqp=CAU",
                "French bulldogs are loveable and cute even if they do make funny noises",
                this
            )
        )

        // This item demos how each recycler item has a lifecycle based on if the view is shown or not
        listData.add(TimerLabelModel())

        // Horizontal Carousel with nested recycler view and adapter. This one has images in different order from one above.
        listData.add(
            HorizontalCarouselModel(
                listOf(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM8xBYMab0g-XvZCPneZ7xkTqupmgRxFOOwA&usqp=CAU",
                    "https://cdn.pixabay.com/user/2012/04/01/00-18-38-212_250x250.png",
                    "https://www.everypixel.com/i/free_1.jpg",
                    "https://images.freeimages.com/images/small-previews/adf/sun-burst-1478549.jpg",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkRMaxRL_zHS6Qz20in4krjpZ077yGYSzmdQ&usqp=CAU"
                )
            )
        )

        _listContent.value = listData
    }

    fun swapPosition(fromPos: Int, toPos: Int) {
        _listContent.value = _listContent.value?.moveItem(fromPos, toPos)
    }

    override fun showToast(body: String) {
        showToast.value = body
    }

    override fun removeItem(model: ImageTextWithButtonsModel) {
        val filteredList = _listContent.value!!.filter { it != model }
        _listContent.value = filteredList
    }

    fun removeItemPosition(indexRemove:Int) {
        val filteredList = listContent.value!!.filterIndexed { index, dynamicModel ->
            index!=indexRemove
        }
        _listContent.value = filteredList
    }

    override fun itemChecked(model: CheckBoxModel) {
        _listContent.value?.filterIsInstance<CheckBoxModel>()?.forEach {
            it.setIsChecked(it == model)
        }
    }
}

fun <T> List<T>.moveItem(fromPos: Int, toPos: Int): List<T> {
    val newList = mutableListOf<T>().apply {
        addAll(this@moveItem)
    }

    newList.removeAt(fromPos)
    newList.add(toPos, this[fromPos])

    return newList
}