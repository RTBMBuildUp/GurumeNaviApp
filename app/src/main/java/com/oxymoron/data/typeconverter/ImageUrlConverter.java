package com.oxymoron.data.typeconverter;

import com.oxymoron.data.room.ImageUrl;

public class ImageUrlConverter implements TypeConverter<ImageUrl, String> {
    @androidx.room.TypeConverter
    @Override
    public String from(ImageUrl wrapper) {
        return wrapper.getUrl().get();
    }

    @androidx.room.TypeConverter
    @Override
    public ImageUrl to(String primitive) {
        return new ImageUrl(primitive);
    }
}
