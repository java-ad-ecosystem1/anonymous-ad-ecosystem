package com.advertisement_sys.model;

import com.advertisement_sys.entity.Advertisement;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean转换工具类
 */
public class BeanUtils {

    /**
     * 将Advertisement实体转换为AdvertisementModel
     */
    public static AdvertisementModel toModel(Advertisement entity) {
        if (entity == null) {
            return null;
        }
        AdvertisementModel model = new AdvertisementModel();
        model.setId(entity.getId() != null ? entity.getId().toString() : null);
        model.setTitle(entity.getTitle());
        model.setAdvertisementType(entity.getAdvertisementType());
        model.setPutType(entity.getPutType());
        model.setPutFlag(entity.getPutFlag());
        model.setContent(entity.getContent());
        model.setCreateTime(entity.getCreateTime());
        return model;
    }

    /**
     * 将Advertisement实体列表转换为AdvertisementModel列表
     */
    public static List<AdvertisementModel> toModelList(List<Advertisement> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<AdvertisementModel> modelList = new ArrayList<>();
        for (Advertisement entity : entityList) {
            AdvertisementModel model = toModel(entity);
            if (model != null) {
                modelList.add(model);
            }
        }
        return modelList;
    }
}

