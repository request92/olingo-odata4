/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.server.core.serializer;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.olingo.commons.api.ODataRuntimeException;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntitySet;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.server.api.serializer.ODataError;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.core.serializer.utils.CircleStreamBuffer;
import org.apache.olingo.server.core.serializer.xml.MetadataDocumentXmlSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataXmlSerializerImpl implements ODataSerializer {

  private static final Logger log = LoggerFactory.getLogger(ODataXmlSerializerImpl.class);

  @Override
  public InputStream serviceDocument(final Edm edm, final String serviceRoot) {
    throw new ODataRuntimeException("Service Document not implemented for XML format");
  }

  @Override
  public InputStream metadataDocument(final Edm edm) {
    CircleStreamBuffer buffer;
    XMLStreamWriter xmlStreamWriter = null;

    // TODO: move stream initialization into separate method
    try {
      buffer = new CircleStreamBuffer();
      xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(buffer.getOutputStream(), DEFAULT_CHARSET);
      MetadataDocumentXmlSerializer serializer = new MetadataDocumentXmlSerializer(edm);
      serializer.writeMetadataDocument(xmlStreamWriter);
      xmlStreamWriter.flush();
      xmlStreamWriter.close();

      return buffer.getInputStream();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ODataRuntimeException(e);
    } finally {
      if (xmlStreamWriter != null) {
        try {
          xmlStreamWriter.close();
        } catch (XMLStreamException e) {
          throw new ODataRuntimeException(e);
        }
      }
    }
  }

  @Override
  public InputStream entity(final EdmEntityType edmEntityType, final Entity entity, final ContextURL contextURL) {
    throw new ODataRuntimeException("Entity serialization not implemented for XML format");
  }

  @Override
  public InputStream entitySet(final EdmEntitySet edmEntitySet, final EntitySet entitySet,
      final ContextURL contextURL) {
    throw new ODataRuntimeException("Entityset serialization not implemented for XML format");
  }

  @Override
  public InputStream error(ODataError error, List<ODataError> details) {
    throw new ODataRuntimeException("error serialization not implemented for XML format");
  }

}
