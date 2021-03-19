const webpack = require('webpack');

module.exports = {
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                CRYPTO_REPORT: JSON.stringify(process.env.CRYPTO_REPORT),
                CRYPTO_USER: JSON.stringify(process.env.CRYPTO_USER)
            }
        })
    ]
}