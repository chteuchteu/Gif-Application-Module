package com.chteuchteu.gifapplicationlibrary.i;

import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.List;

public interface IDataSourceParser {
    public List<Gif> parseDataSource(String dataSourceUrl);
}
