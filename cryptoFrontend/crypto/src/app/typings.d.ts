declare var processEnv: ProcessEnv

interface ProcessEnv {
    env: Env
}

interface Env {
    CRYPTO_REPORT: string,
    CRYPTO_USER: string
}

interface GlobalEnvironment {
    processEnv: ProcessEnv
}