require 'fileutils'

def directories
  dir_project               = File.expand_path(Dir.pwd + '/..')
  dir_project_android_src   = dir_project + '/proj.android/src'
  return [
    dir_project_android_src+"/net"
  ]
end

def symlinks
  dir_project               = File.expand_path(Dir.pwd + '/..')
  dir_project_class         = dir_project + '/Classes'
  dir_project_android_lib   = dir_project + '/proj.android/libs'
  dir_project_android_src   = dir_project + '/proj.android/src'
  dir_nend          = File.expand_path(Dir.pwd + '/nend')
  dir_nend_2dx      = dir_nend + '/nendSDK_cocos2dX_Module_for_v3.2'
  dir_nend_ios      = dir_nend + '/NendSDK_iOS-2.5.5'
  dir_nend_android  = dir_nend + '/NendSDK_Android-2.5.2'
  return {
    # common
    dir_nend_2dx+"/NendModule/NendCommon" => dir_project_class + "/NendCommon",
    # android
    dir_nend_android+"/Nend/SDK/nendSDK-2.5.2.jar" => dir_project_android_lib+"/nendSDK-2.5.2.jar",
    dir_nend_android+"/AdMobMediationAdapter/nendAdapter-1.2.0.jar" => dir_project_android_lib+"/nendAdapter-1.2.0.jar",
    dir_nend_2dx+"/NendModule/JavaPackage/net/nend" => dir_project_android_src+"/net/nend",
    # ios
    dir_nend_ios+"/Nend/NendAd" => dir_project + "/NendAd"
  }
end

def create
  p "create"
  links = symlinks
  dirs = directories
  dirs.each{|dir|
    if !FileTest.exist?(dir)
      p "create directory #{dir}"
      FileUtils.mkdir_p(dir)
    end
  }

  links.each{|from, to|
    p "----------------------------------------------"
    p "#{from} => #{to}"
    if !File.exist?(to)
      if (0 == File.symlink(from, to))
        p "create success"
      else
        p "create failed"
      end
    else
      p "file exists"
    end
  }
end

def remove
  p "remove"
  links = symlinks
  links.each{|from, to|
    p "----------------------------------------------"
    p "#{to}"
    if File.symlink?(to)
      if (File.unlink(to))
        p "unlink success"
      else
        p "unlink failed"
      end
    else
      p "not exists"
    end
  }
end

def main(argv)
  cmd = nil
  if defined? argv[0]
    cmd = argv[0]
  end

  case cmd
  when "create"
    create
  when "remove"
    remove
  when "help"
    help
  else
    help
  end
end

def help
  print <<"EOS"
commands
  create    create symlinks
  remote    remove symlinks
  help      this
EOS
end

main(ARGV)
