package ciris

/**
  * Represents an entry (key-value pair) that has been read from a configuration
  * source. The reading might have failed, represented by wrapping the value in
  * an `Either[ConfigError, String]`. Values are always of type `String`, while
  * the type `Key` can be different. Also includes the [[ConfigKeyType]] to
  * be able to support better error messages.
  *
  * To create a [[ConfigSourceEntry]], use the [[ConfigSourceEntry#apply]] method.
  *
  * {{{
  * scala> ConfigSourceEntry("key", ConfigKeyType.Environment, Right("value"))
  * res0: ConfigSourceEntry[String] = ConfigSourceEntry(key, Environment, Right(value))
  * }}}
  *
  * @param key the key which was read from the configuration source
  * @param keyType the type of keys the configuration source reads
  * @param value the value which was read or a [[ConfigError]]
  * @tparam Key the type of key
  */
sealed class ConfigSourceEntry[Key](
  val key: Key,
  val keyType: ConfigKeyType[Key],
  val value: Either[ConfigError, String]
) {

  /**
    * Transforms the value read from the configuration source by applying
    * the specified function, returning a new [[ConfigSourceEntry]] which
    * includes the modified value.
    *
    * @param f the function to apply to the read configuration value
    * @return a new [[ConfigSourceEntry]] with the transformed value
    * @example {{{
    * scala> val entry = ConfigSourceEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigSourceEntry[String] = ConfigSourceEntry(key, Environment, Right(value ))
    *
    * scala> entry.mapValue(_.trim)
    * res0: ConfigSourceEntry[String] = ConfigSourceEntry(key, Environment, Right(value))
    * }}}
    */
  def mapValue(f: String => String): ConfigSourceEntry[Key] =
    new ConfigSourceEntry(key, keyType, value.right.map(f))

  override def toString: String =
    s"ConfigSourceEntry($key, $keyType, $value)"
}

object ConfigSourceEntry {

  /**
    * Creates a new [[ConfigSourceEntry]] representing an entry (key-value pair)
    * that has been read from a configuration source. The reading might have failed,
    * represented by wrapping the value in an `Either[ConfigError, String]`. Values are
    * always of type `String`, while the `Key` can be different. Also includes the
    * [[ConfigKeyType]] to be able to support better error messages.
    *
    * @param key the key which was read from the configuration source
    * @param keyType the type of keys the configuration source reads
    * @param value the value which was read or a [[ConfigError]]
    * @tparam Key the type of key
    * @return a new [[ConfigSourceEntry]] using the specified arguments
    * @example {{{
    * scala> ConfigSourceEntry("key", ConfigKeyType.Environment, Right("value"))
    * res0: ConfigSourceEntry[String] = ConfigSourceEntry(key, Environment, Right(value))
    * }}}
    */
  def apply[Key](
    key: Key,
    keyType: ConfigKeyType[Key],
    value: Either[ConfigError, String]
  ): ConfigSourceEntry[Key] = {
    new ConfigSourceEntry[Key](key, keyType, value)
  }
}
