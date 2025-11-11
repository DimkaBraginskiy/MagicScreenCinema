package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.example.persistence.declaration.ElementCollection;

class ReferenceTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (rawType.isAnnotationPresent(ElementCollection.class)) {
            return (TypeAdapter<T>) new ReferenceTypeAdapter<>(gson, rawType);
        }
        return null;
    }
}
