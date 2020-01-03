const updateArray = (array, object) => {
  var res = array.map(item => {
    if (item.id !== object.id) {
      return item;
    }
    return Object.assign({}, item, object);
  });
  return res;
};
export default updateArray;
