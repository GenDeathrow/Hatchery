package com.gendeathrow.hatchery.common.data;

public interface ITileDataListener<T> {
    void onChanged(TileDataParameter<T> parameter);
}
