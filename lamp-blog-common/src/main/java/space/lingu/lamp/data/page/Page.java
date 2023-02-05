/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp.data.page;

import java.util.List;

/**
 * @author RollW
 */
public class Page<T> {
    private final int page;// current page
    private final int size;// page size
    private final int total;// total number of items
    private final List<T> data;

    public Page(int page, int size, int total, List<T> data) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotal() {
        return total;
    }

    public List<T> getData() {
        return data;
    }
}
