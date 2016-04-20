/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.watson.developer_cloud.language_translation.v2;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ibm.watson.developer_cloud.http.HttpHeaders;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.language_translation.v2.model.CreateModelOptions;
import com.ibm.watson.developer_cloud.language_translation.v2.model.IdentifiableLanguage;
import com.ibm.watson.developer_cloud.language_translation.v2.model.IdentifiedLanguage;
import com.ibm.watson.developer_cloud.language_translation.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationModel;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.service.ResponseConverter;
import com.ibm.watson.developer_cloud.service.ServiceCall;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * The IBM Watson Language Translation service translate text from one language to another and
 * identifies the language in which text is written.
 *
 * @version v2
 * @see <a href=
 *      "http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/language-translation.html">
 *      Language Translation</a>
 */
public class LanguageTranslation extends WatsonService {

  private static final String SERVICE_NAME = "language_translation";
  private static final String PATH_IDENTIFY = "/v2/identify";
  private static final String PATH_TRANSLATE = "/v2/translate";
  private static final String PATH_IDENTIFIABLE_LANGUAGES = "/v2/identifiable_languages";
  private static final String PATH_MODELS = "/v2/models";
  private static final String BASE_MODEL_ID = "base_model_id";
  private static final String DEFAULT = "default";
  private static final String FORCED_GLOSSARY = "forced_glossary";
  private static final String MODEL_ID = "model_id";
  private static final String MONOLINGUAL_CORPUS = "monolingual_corpus";
  private static final String NAME = "name";
  private static final String PARALLEL_CORPUS = "parallel_corpus";
  private static final String SOURCE = "source";
  private static final String TARGET = "target";
  private static final String TEXT = "text";
  private static final String URL = "https://gateway.watsonplatform.net/language-translation/api";
  private static final String PATH_MODEL = "/v2/models/%s";
  private static final Type TYPE_LIST_TRANSLATION_MODEL = new TypeToken<List<TranslationModel>>() {}.getType();
  private static final Type TYPE_LIST_IDENTIFIABLE_LANGUAGE = new TypeToken<List<IdentifiableLanguage>>() {}.getType();
  private static final Type TYPE_LIST_IDENTIFIED_LANGUAGE = new TypeToken<List<IdentifiedLanguage>>() {}.getType();
  
  /**
   * Instantiates a new Language Translation service.
   */
  public LanguageTranslation() {
    super(SERVICE_NAME);
    setEndPoint(URL);
  }

  /**
   * Creates a translation models.
   *
   * @param options the create model options
   * @return the translation model
   */

  public ServiceCall<TranslationModel> createModel(CreateModelOptions options) {
    Validator.notNull(options, "options cannot be null");
    Validator.notEmpty(options.getBaseModelId(), "options.baseModelId cannot be null or empty");

    final RequestBuilder requestBuilder = RequestBuilder.post(PATH_MODELS);
    requestBuilder.withQuery(BASE_MODEL_ID, options.getBaseModelId());

    if (options.getName() != null)
      requestBuilder.withQuery(NAME, options.getName());

    final MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    // either forced glossary, monolingual corpus or parallel corpus should be specified
    if (options.getForcedGlossary() != null)
      bodyBuilder.addFormDataPart(FORCED_GLOSSARY, options.getForcedGlossary().getName(),
          RequestBody.create(HttpMediaType.BINARY_FILE, options.getForcedGlossary()));
    if (options.getMonolingualCorpus() != null)
      bodyBuilder.addFormDataPart(MONOLINGUAL_CORPUS, options.getMonolingualCorpus().getName(),
          RequestBody.create(HttpMediaType.BINARY_FILE, options.getMonolingualCorpus()));
    if (options.getParallelCorpus() != null)
      bodyBuilder.addFormDataPart(PARALLEL_CORPUS, options.getParallelCorpus().getName(),
          RequestBody.create(HttpMediaType.BINARY_FILE, options.getParallelCorpus()));

    return createServiceCall(requestBuilder.withBody(bodyBuilder.build()).build(),
        ResponseConverterUtils.getObject(TranslationModel.class));
  }

  /**
   * Deletes a translation models.
   *
   * @param modelId the model identifier
   * @return the service call
   */
  public ServiceCall<Void> deleteModel(String modelId) {
    if (modelId == null || modelId.isEmpty())
      throw new IllegalArgumentException("modelId cannot be null or empty");

    Request request = RequestBuilder.delete(String.format(PATH_MODEL, modelId)).build();
    return createServiceCall(request, ResponseConverterUtils.getVoid());
  }

  /**
   * Retrieves the list of identifiable languages.
   *
   * @return the identifiable languages
   * @see TranslationModel
   */
  public ServiceCall<List<IdentifiableLanguage>> getIdentifiableLanguages() {
    final RequestBuilder requestBuilder = RequestBuilder.get(PATH_IDENTIFIABLE_LANGUAGES);

    ResponseConverter<List<IdentifiableLanguage>> converter =
        ResponseConverterUtils.getGenericObject(TYPE_LIST_IDENTIFIABLE_LANGUAGE, "languages");

    return createServiceCall(requestBuilder.build(), converter);

  }

  /**
   * Retrieves a translation models.
   *
   * @param modelId the model identifier
   * @return the translation models
   * @see TranslationModel
   */
  public ServiceCall<TranslationModel> getModel(String modelId) {
    if (modelId == null || modelId.isEmpty())
      throw new IllegalArgumentException("modelId cannot be null or empty");
    Request request = RequestBuilder.get(String.format(PATH_MODEL, modelId)).build();
    return createServiceCall(request, ResponseConverterUtils.getObject(TranslationModel.class));
  }

  /**
   * Retrieves the list of translation models.
   *
   * @return the translation models
   * @see TranslationModel
   */
  public ServiceCall<List<TranslationModel>> getModels() {
    return getModels(null, null, null);
  }

  /**
   * Retrieves the list of models.
   *
   * @param showDefault show default models
   * @param source the source
   * @param target the target
   * @return the translation models
   * @see TranslationModel
   */
  public ServiceCall<List<TranslationModel>> getModels(final Boolean showDefault, final String source,
      final String target) {
    final RequestBuilder requestBuilder = RequestBuilder.get(PATH_MODELS);

    if (source != null && !source.isEmpty())
      requestBuilder.withQuery(SOURCE, source);

    if (target != null && !target.isEmpty())
      requestBuilder.withQuery(TARGET, source);

    if (showDefault != null)
      requestBuilder.withQuery(DEFAULT, showDefault);

    ResponseConverter<List<TranslationModel>> converter =
        ResponseConverterUtils.getGenericObject(TYPE_LIST_TRANSLATION_MODEL, "models");

    return createServiceCall(requestBuilder.build(), converter);
  }

  /**
   * Identify language in which text is written.
   *
   * @param text the text to identify
   * @return the identified language
   */
  public ServiceCall<List<IdentifiedLanguage>> identify(final String text) {
    final RequestBuilder requestBuilder = RequestBuilder.post(PATH_IDENTIFY)
        .withHeader(HttpHeaders.ACCEPT, HttpMediaType.APPLICATION_JSON)
        .withBodyContent(text, HttpMediaType.TEXT_PLAIN);

    ResponseConverter<List<IdentifiedLanguage>> converter =
        ResponseConverterUtils.getGenericObject(TYPE_LIST_IDENTIFIED_LANGUAGE, "languages");

    return createServiceCall(requestBuilder.build(), converter);
  }

  /**
   * Translate text using a model.
   *
   * @param text The submitted paragraphs to translate
   * @param modelId the model id
   * @return The {@link TranslationResult}
   */
  public ServiceCall<TranslationResult> translate(final String text, final String modelId) {
    Validator.isTrue(modelId != null && !modelId.isEmpty(), "modelId cannot be null or empty");
    return translateRequest(text, modelId, null, null);
  }

  /**
   * Translate text using source and target languages.<br>
   * <br>
   * Here is an example of how to translate "hello" from English to Spanish:
   *
   *
   * <pre>
   * LanguageTranslation service = new LanguageTranslation();
   * service.setUsernameAndPassword(&quot;USERNAME&quot;, &quot;PASSWORD&quot;);
   *
   * TranslationResult translationResult = service.translate(&quot;hello&quot;, &quot;en&quot;, &quot;es&quot;);
   *
   * System.out.println(translationResult);
   * </pre>
   *
   * @param text The submitted paragraphs to translate
   * @param source The source language
   * @param target The target language
   * @return The {@link TranslationResult}
   */
  public ServiceCall<TranslationResult> translate(final String text, final Language source, final Language target) {
    return translateRequest(text, null, source, target);
  }

  /**
   * Translate paragraphs of text using a model and or source and target. model_id or source and
   * target needs to be specified. If both are specified, then only model_id will be used
   *
   * @param text the text
   * @param modelId the model id
   * @param source the source
   * @param target the target
   * @return The {@link TranslationResult}
   */
  private ServiceCall<TranslationResult> translateRequest(String text, String modelId, Language source, Language target) {
    Validator.isTrue(text != null && !text.isEmpty(), "text cannot be null or empty");

    final JsonObject contentJson = new JsonObject();

    // convert the text into a json array
    final JsonArray paragraphs = new JsonArray();
    paragraphs.add(new JsonPrimitive(text));
    contentJson.add(TEXT, paragraphs);

    final RequestBuilder requestBuilder =
        RequestBuilder.post(PATH_TRANSLATE).withHeader(HttpHeaders.ACCEPT, HttpMediaType.APPLICATION_JSON);

    if (source != null)
      contentJson.addProperty(SOURCE, source.toString());

    if (target != null)
      contentJson.addProperty(TARGET, target.toString());

    if (modelId != null && !modelId.isEmpty())
      contentJson.addProperty(MODEL_ID, modelId);

    requestBuilder.withBodyJson(contentJson);
    return createServiceCall(requestBuilder.build(), ResponseConverterUtils.getObject(TranslationResult.class));
  }

}
