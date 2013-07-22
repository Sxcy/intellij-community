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
public class GithubUserDetailed extends GithubUser {
  @Nullable private String myName;
  @Nullable private String myEmail;

  private int myOwnedPrivateRepos;

  @NotNull private String myType;
  @NotNull private UserPlan myPlan;

  public static class UserPlan implements Serializable {
    @NotNull private String myName;
    private long myPrivateRepos;

    @SuppressWarnings("ConstantConditions")
    protected UserPlan(@NotNull GithubUserRaw.UserPlanRaw raw) {
      myName = raw.name;
      myPrivateRepos = raw.privateRepos;
    }

    @NotNull
    public String getName() {
      return myName;
    }

    public long getPrivateRepos() {
      return myPrivateRepos;
    }
  }

  public boolean canCreatePrivateRepo() {
    return getPlan().getPrivateRepos() > getOwnedPrivateRepos();
  }

  @NotNull
  @SuppressWarnings("ConstantConditions")
  public static GithubUserDetailed createDetailed(@Nullable GithubUserRaw raw) throws JsonException {
    try {
      return new GithubUserDetailed(raw);
    }
    catch (IllegalArgumentException e) {
      throw new JsonException("GithubUserDetailed parse error", e);
    }
  }

  @SuppressWarnings("ConstantConditions")
  private GithubUserDetailed(@NotNull GithubUserRaw raw) {
    super(raw);
    myName = raw.name;
    myEmail = raw.email;
    myType = raw.type;
    myOwnedPrivateRepos = raw.ownedPrivateRepos;
    myPlan = new UserPlan(raw.plan);
  }

  @Nullable
  public String getName() {
    return myName;
  }

  @Nullable
  public String getEmail() {
    return myEmail;
  }

  @NotNull
  public String getType() {
    return myType;
  }

  public int getOwnedPrivateRepos() {
    return myOwnedPrivateRepos;
  }

  @NotNull
  public UserPlan getPlan() {
    return myPlan;
  }
}
