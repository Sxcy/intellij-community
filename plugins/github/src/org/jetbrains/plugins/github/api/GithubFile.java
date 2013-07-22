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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author Aleksey Pivovarov
 */
@SuppressWarnings("UnusedDeclaration")
public class GithubFile implements Serializable {
  @NotNull private String myFilename;

  private int myAdditions;
  private int myDeletions;
  private int myChanges;
  @NotNull private String myStatus;

  @NotNull private String myRawUrl;
  @NotNull private String myPatch;

  @NotNull
  @SuppressWarnings("ConstantConditions")
  public static GithubFile create(@Nullable GithubFileRaw raw) throws JsonException {
    try {
      return new GithubFile(raw);
    }
    catch (IllegalArgumentException e) {
      throw new JsonException("CommitFile parse error", e);
    }
  }

  @SuppressWarnings("ConstantConditions")
  protected GithubFile(@NotNull GithubFileRaw raw) {
    myFilename = raw.filename;
    myAdditions = raw.additions;
    myDeletions = raw.deletions;
    myChanges = raw.changes;
    myStatus = raw.status;
    myRawUrl = raw.rawUrl;
    myPatch = raw.patch;
  }

  @NotNull
  public String getFilename() {
    return myFilename;
  }

  public int getAdditions() {
    return myAdditions;
  }

  public int getDeletions() {
    return myDeletions;
  }

  public int getChanges() {
    return myChanges;
  }

  @NotNull
  public String getStatus() {
    return myStatus;
  }

  @NotNull
  public String getRawUrl() {
    return myRawUrl;
  }

  @NotNull
  public String getPatch() {
    return myPatch;
  }
}
