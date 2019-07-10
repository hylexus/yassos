# YASSOS 
## docs

YASSOS (Yet Another Single Sign On System)

test github-pages

```java
    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator.SimpleUUIDTokenGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(CredentialsMatcher.class)
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher.PlainTextCredentialsMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(SessionInfoEnhancer.class)
    public SessionInfoEnhancer sessionInfoEnhancer() {
        return new SessionInfoEnhancer.NoneEnhancementEnhancer();
    }

    @Bean
    @ConditionalOnMissingBean(YassosSessionAttrConverter.class)
    public YassosSessionAttrConverter yassosSessionAttrConverter() {
        return new YassosSessionAttrConverter.SimpleYassosSessionAttrConverter();
    }

```
