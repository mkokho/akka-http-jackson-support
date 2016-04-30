/*
 * Copyright 2016  Mikhail Kokho
 *
 * Licensed under the MIT License (MIT)
 */

package nz.mkokho.akkahttpjackson

import java.io.StringWriter

import scala.reflect.ClassTag

import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.fasterxml.jackson.databind.ObjectMapper

/**
  * A trait providing automatic to and from JSON marshalling/unmarshalling.
  *
  * All you need to do is to instantiate jacksonObjectMapper and register DefaultScalaModule
  * Example:
  *
     object JacksonSupport extends JacksonJsonSupport {
       val jacksonObjectMapper = new ObjectMapper()
       jacksonObjectMapper.registerModule(DefaultScalaModule)
       //your custom configurations
       //jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
     }
  */
trait JacksonJsonSupport {

  def jacksonObjectMapper: ObjectMapper

  implicit def jacksonJsonUnmarshaller[T: ClassTag]: FromEntityUnmarshaller[T] = {
    Unmarshaller.byteStringUnmarshaller.forContentTypes(`application/json`).mapWithCharset { (data, charset) ⇒
      val input = data.decodeString(charset.nioCharset.name)
      deserialize[T](input)
    }
  }

  implicit def jacksonJsonMarshaller[T <: AnyRef]: ToEntityMarshaller[T] = {
    Marshaller.withFixedContentType(`application/json`) { any =>
      HttpEntity(`application/json`, serialize(any))
    }
  }

  def serialize[T](any: T): String = {
    val value = new StringWriter
    jacksonObjectMapper.writeValue(value, any)
    value.toString
  }

  def deserialize[T: ClassTag](blob: String): T = {
    jacksonObjectMapper.readValue(blob, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
  }

}
