/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.github.api;

import com.intellij.util.containers.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jetbrains.plugins.github.api.GithubGistRaw.GistFileRaw;

/**
 * @author Aleksey Pivovarov
 */
public class GithubGist {
  @NotNull private String myId;
  @NotNull private String myDescription;

  private boolean myIsPublic;

  @NotNull private String myHtmlUrl;

  @NotNull private List<GistFile> myFiles;

  @Nullable private GithubUser myUser;

  public static class GistFile {
    @NotNull private String myFilename;
    @NotNull private String myContent;

    @NotNull private String myRawUrl;

    @SuppressWarnings("ConstantConditions")
    protected GistFile(@NotNull GistFileRaw raw) {
      myFilename = raw.filename;
      myContent = raw.content;
      myRawUrl = raw.raw_url;
    }

    @NotNull
    public String getFilename() {
      return myFilename;
    }

    @NotNull
    public String getContent() {
      return myContent;
    }

    @NotNull
    public String getRawUrl() {
      return myRawUrl;
    }
  }

  @NotNull
  public Map<String, String> getContent() {
    Map<String, String> ret = new HashMap<String, String>();
    for (GistFile file : getFiles()) {
      ret.put(file.getFilename(), file.getContent());
    }
    return ret;
  }

  @NotNull
  @SuppressWarnings("ConstantConditions")
  public static GithubGist create(@Nullable GithubGistRaw raw) throws JsonException {
    try {
      return new GithubGist(raw);
    }
    catch (IllegalArgumentException e) {
      throw new JsonException("GithubGist parse error", e);
    }
    catch (JsonException e) {
      throw new JsonException("GithubGist parse error", e);
    }
  }

  @SuppressWarnings("ConstantConditions")
  protected GithubGist(@NotNull GithubGistRaw raw) throws JsonException {
    myId = raw.id;
    myDescription = raw.description;
    myIsPublic = raw.isPublic;
    myHtmlUrl = raw.htmlUrl;
    myUser = raw.user == null ? null : GithubUser.create(raw.user);

    if (raw.files == null) throw new JsonException("files is null");
    myFiles = new ArrayList<GistFile>();
    for (Map.Entry<String, GistFileRaw> rawFile : raw.files.entrySet()) {
      myFiles.add(new GistFile(rawFile.getValue()));
    }
  }

  @NotNull
  public String getId() {
    return myId;
  }

  @NotNull
  public String getDescription() {
    return myDescription;
  }

  public boolean isPublic() {
    return myIsPublic;
  }

  @NotNull
  public String getHtmlUrl() {
    return myHtmlUrl;
  }

  @NotNull
  public List<GistFile> getFiles() {
    return myFiles;
  }

  @Nullable
  public GithubUser getUser() {
    return myUser;
  }
}
