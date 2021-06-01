package com.teegarcs.dynamicadapterexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teegarcs.dynamicadapter.DynamicModel

class MainViewModel : ViewModel(), ImageTextButtonActions, CheckBoxAction {
    private val _listContent = MutableLiveData<List<DynamicModel>>().apply { value = listOf() }
    val listContent: LiveData<List<DynamicModel>> = _listContent

    init {
        val listData = mutableListOf<DynamicModel>()

        listData.add(TimerLabelModel())
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))
        listData.add(CheckBoxModel(this))
        listData.add(
            ImageTextModel(
                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1c/04/57/d9/marriott-resort-virginia.jpg?w=900&h=-1&s=1",
                "As travellers worldwide dream of escaping on holiday, Marriott Bonvoy is expanding in some of the most desirable leisure destinations across Europe and The United Arab Emirates."
            )
        )

        listData.add(
            ImageTextWithButtonsModel(
                "https://bsmedia.business-standard.com/_media/bs/img/article/2020-07/25/full/1595618904-5908.jpg",
                "Google in November this year revised its storage policies for Gmail, Drive and Photos. The updated policies will not take effect until June 1, 2021.",
                this
            )
        )


        listData.add(
            ImageTextModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMm5N6vcL6IGP6-MW9DTYxDINtYwcLhCpfGA&usqp=CAU",
                "Learn at your own pace, with flexible and personalised training courses designed to build your confidence and help you thrive."
            )
        )

        listData.add(
            ImageTextWithButtonsModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkRMaxRL_zHS6Qz20in4krjpZ077yGYSzmdQ&usqp=CAU",
                "Dogs are known to surf if their owner loves to surf. Dogs are very keen to staying dry.",
                this
            )
        )

        listData.add(
            ImageTextWithButtonsModel(
                "https://images.freeimages.com/images/small-previews/adf/sun-burst-1478549.jpg",
                "The sun peaking through a cloud covered sky.",
                this
            )
        )

        listData.add(
            ImageTextWithButtonsModel(
                "https://cdn.pixabay.com/user/2012/04/01/00-18-38-212_250x250.png",
                "A wonderful cartoon of a fish with many different colors.",
                this
            )
        )

        listData.add(
            ImageTextModel(
                "https://www.everypixel.com/i/free_1.jpg",
                "A majestic bird of prey"
            )
        )

        listData.add(
            ImageTextWithButtonsModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSM8xBYMab0g-XvZCPneZ7xkTqupmgRxFOOwA&usqp=CAU",
                "French bulldogs are loveable and cute even if they do make funny noises",
                this
            )
        )

        listData.add(TimerLabelModel())

        _listContent.value = listData
    }

    override fun showToast(body: String) {

    }

    override fun removeItem(model: ImageTextWithButtonsModel) {
        val filteredList = _listContent.value!!.filter { it != model }
        _listContent.value = filteredList
    }

    override fun itemChecked(model: CheckBoxModel) {
        _listContent.value?.forEach {
            if (it is CheckBoxModel) {
                it.setIsChecked(it == model)
            }
        }
    }
}