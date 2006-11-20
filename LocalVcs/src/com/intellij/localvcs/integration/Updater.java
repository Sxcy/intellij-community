package com.intellij.localvcs.integration;

import com.intellij.localvcs.Entry;
import com.intellij.localvcs.LocalVcs;
import com.intellij.localvcs.Path;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

public class Updater {
  public static void update(LocalVcs vcs, VirtualFile root) throws IOException {
    // todo should root changes become vesible only after apply?
    vcs.getRoot().setPath(root.getPath());

    // todo test that deleting called first to ensure that sush cases as deleting file 'a'
    // todo and creating dir 'a' are handled correctly

    createNewFiles(vcs, root);
    deleteAbsentFiles(vcs, vcs.getRoot(), root);

    vcs.apply();
  }

  private static void createNewFiles(LocalVcs vcs, VirtualFile dir) throws IOException {
    for (VirtualFile f : dir.getChildren()) {
      if (!vcs.hasEntry(new Path(f.getPath()))) {
        if (f.isDirectory()) {
          vcs.createDirectory(new Path(f.getPath()));
          createNewFiles(vcs, f);
        }
        else {
          vcs.createFile(new Path(f.getPath()), new String(f.contentsToByteArray()));
        }
      }
    }
  }

  private static void deleteAbsentFiles(LocalVcs vcs, Entry entry, VirtualFile dir) {
    for (Entry e : entry.getChildren()) {
      // todo somethig is going wrong with Path abstraction
      // todo move this check to VirtualFile
      VirtualFile f = dir.findChild(e.getPath().getName());

      if (f == null) {
        vcs.delete(e.getPath());
      }
      else {
        if (e.isDirectory()) {
          deleteAbsentFiles(vcs, e, f);
        }
      }
    }
  }
}
