package com.festago.festago.data.datasource

interface TokenDataSource {
    var token: String?
    var socialType: String
    var socialToken: String
}
