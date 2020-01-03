const getValueByProperty = (object, properties) => {
  var result = object;
  properties.map(prop => {
    result = result[prop];
  });
  return result;
};

const groupByProperty = (tasks, groups, properties) => {
  var splitProps = properties.split(".");
  tasks.map((value, index, array) => {
    const groupName = getValueByProperty(value, splitProps);
    var groupExists =
      groups.filter(group => {
        return group.name === groupName;
      }).length > 0;
    if (!groupExists) {
      const newGroup = { name: groupName, data: [] };
      groups = groups.concat([newGroup]);
    }
    groups
      .find(group => {
        return group.name === groupName;
      })
      .data.push(value);
  });
  return groups;
};

export const groupByObject = (tasks, groups, properties) => {
  var splitProps = properties.split(".");
  tasks.map((value, index, array) => {
    const object = getValueByProperty(value, splitProps);
    var groupExists =
      groups.filter(group => {
        return group.object.id === object.id;
      }).length > 0;
    if (!groupExists) {
      const newGroup = { object: object, data: [] };
      groups = groups.concat([newGroup]);
    }
    groups
      .find(group => {
        return group.object.id === object.id;
      })
      .data.push(value);
  });
  return groups;
};

export default groupByProperty;
