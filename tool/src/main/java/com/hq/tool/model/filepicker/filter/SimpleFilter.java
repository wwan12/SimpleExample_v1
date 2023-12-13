/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.hq.tool.model.filepicker.filter;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;


import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 */
public class SimpleFilter implements FileFilter {
    private final boolean isOnlyDir;
    private final String[] allowExtensions;

    public SimpleFilter(boolean isOnlyDir, @Nullable String[] allowExtensions) {
        this.isOnlyDir = isOnlyDir;
        this.allowExtensions = allowExtensions;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname == null) {
            Log.e("error","Filter>>>pathname is null");
            return false;
        }
        if (pathname.isDirectory()) {
            Log.e("error","Filter>>>pathname is directory: " + pathname);
            return true;
        }
        if (isOnlyDir && pathname.isFile()) {
            Log.e("error","Filter>>>except directory but is file: " + pathname);
            return false;
        }
        if (allowExtensions == null || allowExtensions.length == 0) {
            Log.e("error","Filter>>>allow extensions is empty: " + pathname);
            return true;
        }
        //返回当前目录所有以某些扩展名结尾的文件
        String extension = getExtension(pathname.getPath());
        Log.e("back","Filter>>>extension of " + pathname + ": " + extension);
        boolean contains = false;
        for (String allowExtension : allowExtensions) {
            if (TextUtils.isEmpty(allowExtension) || allowExtension.contains(extension)) {
                contains = true;
                break;
            }
        }
        Log.e("back","Filter>>>allow extensions is " + Arrays.toString(allowExtensions) + ", contains: " + contains);
        return contains;
    }

    private String getExtension(String path) {
        if (path == null) {
            return "";
        }
        String ext = "";
        int slashPos = path.lastIndexOf(File.separator);
        if (slashPos != -1) {
            path = path.substring(slashPos);
        }
        int dotPos = path.lastIndexOf('.');
        if (dotPos != -1) {
            ext = path.substring(dotPos + 1);
        } else {
            ext = path;
        }
        return ext;
    }

}
