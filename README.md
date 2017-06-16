#What is it

This is one part of the test automation framework targeting testing web pages.
Internally it uses selenium to perform execution via browser. This part is 
called core, which is abstraction layer to be used by tests it gives two jar 
files (modules).

todo: complete me.

The framework is made with two jars. Architecture briefly follows
``` 
---------------------------------
|   ------    -----------------  |
|  | core |->| container Layer | |
|   ------    -----------------  |
|    |             |             |
|    |             |             |
|   API           API            |
|    |             |             |
|  ----------------------------- |
| |  test scrips for domains    ||
|  ----------------------------- |
----------------------------------
```

In short - this framework provides DSL or simplified programming model 
to write the test with abstraction on the tools used targeting web pages 
testing.

---

##Developers guide
###Requirements
* [git](https://git-scm.com/downloads) `brew install git`
* [jdk 1.8] (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) `brew install java8`
* [IntelliJ] (https://www.jetbrains.com/idea/download/)  or any other IDE you like.
* [maven] (https://maven.apache.org/download.cgi) > v3. `brew install maven`
* VPN or being in internal network (for repository). `ping sd1pgo11lx.saksdirect.com`
 
###  How to make changes
Just open it in any IDE you want as maven project and make your changes.
Your local copy will have snapshot version so you can adjust your tests to 
use your local generated artifact. pom file should guide you with any other 
details should you need.

If you are lazy to go through all the steps, use following commands to clone 
everything related to test automation. Use git-bash or some terminal on Unix/Linux

```bash

mkdir ta && cd ta
# Framework
git clone git@github.com:saksdirect/ta-common-banner.git
git clone git@github.com:saksdirect/ta-framework-core.git

# Scripts
git clone git@github.com:saksdirect/ta-saks-fifth-tests.git
git clone git@github.com:saksdirect/ta-off-fifth-tests.git
git clone git@github.com:saksdirect/ta-dsg-tests.git

# let's build and install development version

cd ta-common-banner && mvn install -T2C && cd ..
cd ta-framework-core && mvn install -T2C && cd ..

cd ta-saks-fifth-tests && mvn install -T2C && cd ..
cd ta-off-fifth-tests && mvn install -T2C && cd ..
cd ta-dsg-tests && mvn install -T2C && cd ..

```

This will clone and build all repositories in folder named `ta`. You can import those project 
one by one into your IDE and start working.

<em>Important notes about versions</em>

## Branches and repositories
The framework part uses the gitflow model - you make your changes in your branch and merge
into `dev` branch. Master is untouched and will contain only stable versions. `dev` branch is
default for `ta-common-banner` and `ta-framework-core` repositories.

Tests repositories:
* `ta-saks-fifth-tests`
* `ta-off-fifth-tests`
* `ta-dsg-tests`

using single branch where you commit changes into your branch and merge into `master`.

## Branching strategies for framework
The framework is versioned with simplified git-flow model, with `dev` and `master` 
branches. `dev` branch is default and development branch responsible for taking 
care of current development. `master` branch is dedicated to hold only 
releases - stable versions. You should never need commit or merge into `master` branch
in normal circumstances.

Please do not commit to neither of `dev` or `master` branches - it will break the 
branching model and makes impossible/hard to revert code back. All changes should be
made to your branch and be pushed to github.

###Working wth git and making changes to framework
When you checkout repositories as instructed above - you will have latest development version
of the framework. From this point you can start making changes and debug locally on your machine.

Similar to git-flow model, all your changes has to go through the pull request.
Follow steps below in your daily development:
```bash
# switch to development branch
git checkout dev

#pull any changes if there are any
git pull

# make your branch

git checkout -b my-branch

# make your changes - your development

# commit and push

git commit

git push -u origin my-branch 
```

You can do same steps in IDE of your like. Just make sure you do NOT commit directly, 
but using [pull requests] (https://help.github.com/articles/using-pull-requests).

Read following pages to get general knowledge about the branching model
* http://nvie.com/posts/a-successful-git-branching-model/
* https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow/


In addition, you should not commit changes to in `pom.xml` with your branch. Latest
development version can be found in the release link in github. Development version will
have `SNAPSHOT` postfix.

##Releasing new version

Releases are made only from `dev` branch - when dev branch is stable enough and includes
all planned changes. Merge into master happens when release performed on `dev` branch. 
The merge performed by CI server - no human interaction.

To release new version of the framework go to:
http://10.32.150.35:8080/project.html?projectId=TaFramework_TaCi&tab=projectOverview
review list of the changes and run the build - it will release new version of the
framework and publish artifacts into repository leading with bumping up to next version.
You will need to update team saying new version have been released and they can switch to 
new version in their poms.

When you release `ta-common-banner` or `ta-framework-core` team will need to update
dependencies of scripts projects manually  to use the latest released version.

`ta-common-banner` and `ta-framework-core` can be released separately, however,
all dependencies on `ta-framework-core` should be updated manually. This is made intentionally
to separate development and released versions, so that new development will not harm existing
scripts.

## Continuous integration

All test automation are using CI based on maven. Artifacts repository is 
[http://www.jfrog.com/artifactory/](Artifactory) can be found at 
http://sd1pgo11lx.saksdirect.com:8081/artifactory. Build server can be found 
at http://10.32.150.35:8080/. See Contacts section for creating user account
in artifactory, CI server and the github.

Release process is automated and does not requires human interaction.
Following build configuration is responsible to release framework artifacts:
`http://10.32.150.35:8080/project.html?projectId=TaFramework_TaCi&tab=projectOverview`
Triggering this builds will release new version of the framework - the core and the
container layer accordingly.

Builds at http://10.32.150.35:8080/project.html?projectId=ta_dev_builds&tab=projectOverview
are responsible to monitor development status. The pull request are automatically validated
by build server. Your task as developer in the team is to make sure you are not breaking build
with your changes and all developer builds are green.

Full hierarchy of test automation projects can be seen at http://10.32.150.35:8080/project.html?projectId=TaFramework&tab=projectOverview


# Contacts
* Accounts in github: Glenn_Larry@s5a.com
* Account in Build server: vevin_moze@s5a.com
* Repository/Artifactory account Matt Pick.
