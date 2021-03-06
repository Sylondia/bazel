// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.rules.objc;

import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.concurrent.ThreadSafety.Immutable;
import com.google.devtools.build.lib.packages.NativeInfo;
import com.google.devtools.build.lib.packages.NativeProvider;
import com.google.devtools.build.lib.skylarkinterface.SkylarkCallable;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModule;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModuleCategory;

/**
 * Provider containing the executable binary output that was built using an apple_binary target with
 * the 'dylib' type. This provider contains:
 *
 * <ul>
 *   <li>'binary': The dylib artifact output by apple_binary
 *   <li>'objc': An {@link ObjcProvider} which contains information about the transitive
 *       dependencies linked into the dylib, (intended so that binaries depending on this dylib may
 *       avoid relinking symbols included in the dylib
 * </ul>
 */
@Immutable
@SkylarkModule(
    name = "AppleDylibBinary",
    category = SkylarkModuleCategory.PROVIDER,
    doc = "A provider containing the executable binary output that was built using an apple_binary "
        + "target with the 'dylib' type."
)
public final class AppleDylibBinaryInfo extends NativeInfo {

  /** Skylark name for the AppleDylibBinaryInfo. */
  public static final String SKYLARK_NAME = "AppleDylibBinary";

  /** Skylark constructor and identifier for AppleDylibBinaryInfo. */
  public static final NativeProvider<AppleDylibBinaryInfo> SKYLARK_CONSTRUCTOR =
      new NativeProvider<AppleDylibBinaryInfo>(AppleDylibBinaryInfo.class, SKYLARK_NAME) {};

  private final Artifact dylibBinary;
  private final ObjcProvider depsObjcProvider;

  public AppleDylibBinaryInfo(Artifact dylibBinary,
      ObjcProvider depsObjcProvider) {
    super(SKYLARK_CONSTRUCTOR);
    this.dylibBinary = dylibBinary;
    this.depsObjcProvider = depsObjcProvider;
  }

  /**
   * Returns the multi-architecture dylib binary that apple_binary created.
   */
  @SkylarkCallable(name = "binary",
      structField = true,
      doc = "The dylib file output by apple_binary."
  )
  public Artifact getAppleDylibBinary() {
    return dylibBinary;
  }

  /**
   * Returns the {@link ObjcProvider} which contains information about the transitive dependencies
   * linked into the dylib.
   */
  @SkylarkCallable(name = "objc",
      structField = true,
      doc = "A provider which contains information about the transitive dependencies linked into "
          + "the dylib, (intended so that binaries depending on this dylib may avoid relinking "
          + "symbols included in the dylib."
  )
  public ObjcProvider getDepsObjcProvider() {
    return depsObjcProvider;
  }
}
