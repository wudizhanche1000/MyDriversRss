package org.weijian.mydriversrss

import java.util.*

/**
 * Created by weijian on 16-4-12.
 */

data class News(var title: String? = null, var link: String? = null, var sourceUrl: String? = null,
                var editor: String? = null, var picCount: Int = 0, var reviewCount: Int = 0,
                var comment: String? = null, var images: Array<String>? = null, var articleId: Int? = null,
                var pubTime: Date? = null) {
}

