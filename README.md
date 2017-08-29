# Shortcut-interface
## What is it for?
Nothing really. It was just a fun passion project for me when I was on my summer vacation.
## What does it do
It creates keyboard shortcuts. Shortcuts are automatically written by your choosen key / action. Shortcuts are written in [AHK](https://autohotkey.com/) macro language.
## How does it work
1. You open the application you see a weird looking starting screen. You press the plus button to make a new script.

2. Then you choose your key / keys you want. Then you press next or the second step on the stepbar.

3. Then you choose your action / actions you want. Press save and you have a shortcut script. 

4. You can run your scripts in previously mentioned weird looking starting screen (bottom left of the starting screen has a button run). 

<template name="fileList">
  <div class="fileList">
    {{#each files}}
      <div class="file">
        <strong>{{this.name}}</strong> <a href="{{this.url download=true}}" class="btn btn-primary" target="_parent">Download</a>
      </div>
    {{/each}}
  </div>
</template>
