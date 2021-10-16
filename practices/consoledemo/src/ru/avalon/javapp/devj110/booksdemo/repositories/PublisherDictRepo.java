package ru.avalon.javapp.devj110.booksdemo.repositories;

import ru.avalon.javapp.devj110.booksdemo.models.Book;
import ru.avalon.javapp.devj110.booksdemo.models.Publisher;

public class PublisherDictRepo {
    private Publisher[] publisherStore;

    public PublisherDictRepo(){
        publisherStore = new Publisher[]{
                new Publisher("Проспект", "Москва"),
                new Publisher("Питер", "Санкт-Петербург"),
                new Publisher("БХВ", "Санкт-Петебург"),
                new Publisher("Диалектика", "Киев")
        };
    }

    public Publisher getItemByIdx(int idx){
        if ((idx <0)||(idx>=publisherStore.length)){
            throw new IndexOutOfBoundsException("Illegal index passed");
        }
        return publisherStore[idx];
    }
}
